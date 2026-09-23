"""Repository-navigation tools exposed to the porter agent, so it can pull in
exactly the code it needs (the source rule, the DSL definitions) on demand
instead of everything being stuffed into one prompt up front. This is what
keeps a rule with a long/complex source implementation from blowing past a
small model's context window, and lets the agent verify its own assumptions
about the DSL by reading the real files rather than trusting a paraphrase.

Two read-only source roots ("calcite" — the vendored upstream source rules
are ported *from*; "rulescript" — the DSL + previously-proved rules), plus
two actions that mutate state: `try_rule` (the actual compile/JSON/qed-prover
pipeline for the one rule file being ported) and `extend_dsl_file` (a rare,
regression-gated way to grow the DSL itself — RelRN.java/RexRN.java/
JSONSerializer.java — when a rule genuinely needs an operator that isn't
exposed yet). Both let the agent iterate on its own schedule rather than the
harness force-feeding a fixed retry script.

Security note: every path-taking tool resolves against its root and refuses
to serve anything outside it (no `../` escapes) — the agent only ever reads
files that were already vendored on disk by this project's setup. Writes are
limited to the one rule file (`try_rule`) and, for `extend_dsl_file`, exactly
the three named DSL files — never the QED prover, which isn't reachable from
either root at all. Every `extend_dsl_file` call is gated by a full project
recompile plus re-proving every already-proved rule; either check failing
reverts the file automatically.
"""
from __future__ import annotations

import json
import subprocess
from dataclasses import dataclass, field
from pathlib import Path

from pipeline import Pipeline

MAX_READ_LINES = 400
MAX_SEARCH_RESULTS = 60

EXTENDABLE_DSL_FILES = ("RelRN.java", "RexRN.java", "JSONSerializer.java")


class ToolError(Exception):
    pass


def _resolve(root: Path, rel_path: str) -> Path:
    candidate = (root / rel_path).resolve()
    try:
        candidate.relative_to(root.resolve())
    except ValueError:
        raise ToolError(f"path {rel_path!r} escapes the allowed root") from None
    return candidate


@dataclass
class RepoTools:
    rulescript_root: Path
    calcite_root: Path
    pipeline: Pipeline
    rule_name: str
    json_out_dir: Path
    docs_root: Path | None = None
    baseline_proved_rules: tuple[str, ...] = ()
    dsl_edits: list = field(default_factory=list)

    def _root(self, root: str) -> Path:
        if root == "rulescript":
            return self.rulescript_root
        if root == "calcite":
            return self.calcite_root
        if root == "docs" and self.docs_root is not None:
            return self.docs_root
        raise ToolError(f"unknown root {root!r}, must be 'rulescript', 'calcite', or 'docs'")


    def list_directory(self, root: str, path: str = ".") -> dict:
        base = self._root(root)
        target = _resolve(base, path)
        if not target.exists():
            return {"error": f"{path} does not exist under {root}"}
        if target.is_file():
            return {"error": f"{path} is a file, not a directory"}
        entries = sorted(p.name + ("/" if p.is_dir() else "") for p in target.iterdir()
                          if not p.name.startswith("."))
        return {"path": path, "entries": entries}

    def search_code(self, root: str, query: str, path: str = ".") -> dict:
        base = self._root(root)
        target = _resolve(base, path)
        result = subprocess.run(
            ["grep", "-rn", "--include=*.java", "-E", query, str(target)],
            capture_output=True, text=True, timeout=30,
        )
        lines = result.stdout.splitlines()
        truncated = len(lines) > MAX_SEARCH_RESULTS
        lines = lines[:MAX_SEARCH_RESULTS]
        rel_lines = [line.replace(str(base) + "/", "", 1) for line in lines]
        return {
            "matches": rel_lines,
            "truncated": truncated,
            "note": "narrow your query or path" if truncated else "",
        }

    def find_symbol(self, root: str, symbol: str) -> dict:
        base = self._root(root)
        pattern = (
            rf"\b(class|interface|record|enum)\s+{symbol}\b|"
            rf"\b{symbol}\s*\("
        )
        result = subprocess.run(
            ["grep", "-rn", "--include=*.java", "-E", pattern, str(base)],
            capture_output=True, text=True, timeout=30,
        )
        lines = result.stdout.splitlines()
        truncated = len(lines) > MAX_SEARCH_RESULTS
        lines = lines[:MAX_SEARCH_RESULTS]
        rel_lines = [line.replace(str(base) + "/", "", 1) for line in lines]
        return {"matches": rel_lines, "truncated": truncated}

    def read_file(self, root: str, path: str, start_line: int | None = None, end_line: int | None = None) -> dict:
        base = self._root(root)
        target = _resolve(base, path)
        if not target.exists() or not target.is_file():
            return {"error": f"{path} is not a readable file under {root}"}
        lines = target.read_text(errors="replace").splitlines()
        total = len(lines)
        start = max(1, start_line or 1)
        end = min(total, end_line or (start + MAX_READ_LINES - 1))
        if end - start + 1 > MAX_READ_LINES:
            end = start + MAX_READ_LINES - 1
        numbered = "\n".join(f"{i}\t{lines[i - 1]}" for i in range(start, end + 1))
        return {
            "path": path, "total_lines": total, "start_line": start, "end_line": end,
            "content": numbered,
            "note": "" if end >= total else f"file continues past line {end}; call again with a later start_line",
        }

    def read_pdf(self, path: str, start_line: int | None = None, end_line: int | None = None) -> dict:
        """Extract text from a PDF under the 'docs' root via `pdftotext` (poppler)
        and page through it exactly like read_file. Kept as a separate tool
        (rather than folded into read_file) since PDF extraction is slow
        enough to be worth calling out explicitly, and fails in its own way
        (missing `pdftotext` binary) that read_file never has to handle."""
        if self.docs_root is None:
            return {"error": "docs root not configured"}
        target = _resolve(self.docs_root, path)
        if not target.exists() or not target.is_file():
            return {"error": f"{path} is not a readable file under docs"}
        if target.suffix.lower() != ".pdf":
            return {"error": f"{path} is not a .pdf file; use read_file for plain text"}
        try:
            result = subprocess.run(
                ["pdftotext", "-layout", str(target), "-"],
                capture_output=True, text=True, timeout=60,
            )
        except FileNotFoundError:
            return {"error": "pdftotext (poppler) is not installed on this host; cannot extract PDF text"}
        if result.returncode != 0:
            return {"error": f"pdftotext failed: {result.stderr[:1000]}"}
        lines = result.stdout.splitlines()
        total = len(lines)
        start = max(1, start_line or 1)
        end = min(total, end_line or (start + MAX_READ_LINES - 1))
        if end - start + 1 > MAX_READ_LINES:
            end = start + MAX_READ_LINES - 1
        numbered = "\n".join(f"{i}\t{lines[i - 1]}" for i in range(start, end + 1))
        return {
            "path": path, "total_lines": total, "start_line": start, "end_line": end,
            "content": numbered,
            "note": "" if end >= total else f"file continues past line {end}; call again with a later start_line",
        }

    def try_rule(self, java_source: str) -> dict:
        """Write the candidate, compile it, serialize to QED's JSON, and run the
        real qed-prover. This is the ONE tool that actually mutates/tests state;
        everything else here is read-only exploration."""
        self.pipeline.write_rule(self.rule_name, java_source)
        compile_result = self.pipeline.compile()
        if not compile_result.ok:
            return {"stage": "compile", "ok": False, "output": compile_result.output[:3000]}

        json_result, json_path = self.pipeline.generate_json(self.rule_name, self.json_out_dir)
        if not json_result.ok or not json_path.exists():
            return {"stage": "json_generation", "ok": False, "output": json_result.output[:3000]}

        prover_result, parsed = self.pipeline.run_prover(json_path)
        if parsed is None:
            return {"stage": "prove", "ok": False, "output": prover_result.output[:3000]}
        result = {"stage": "prove", "ok": True, "provable": parsed.get("provable"),
                  "result": parsed, "json_path": str(json_path)}
        if not parsed.get("provable"):
            result["hint"] = (
                "Not provable does not necessarily mean this rule is unsupported. Common "
                "fixable causes: (1) a name mismatch — a predicate/projection/table that "
                "should be the same uninterpreted symbol in before() and after() was given "
                "two different names, or vice versa; (2) a merge-two-operators rule needs "
                "function composition (chaining .proj()) rather than a fresh independent "
                "symbol; (3) a needed primary-key uniqueness, join type, or set-vs-bag "
                "union/intersect/minus flag doesn't match the source rule. If none of that "
                "applies and you believe this reflects a genuine QED limitation, see the "
                "reference's Limitations and 'Declaring scope honestly' sections."
            )
        return result

    def extend_dsl_file(self, file: str, old_snippet: str, new_snippet: str, reason: str) -> dict:
        """Find-and-replace edit on one of the three DSL-defining files (like a
        normal code-editing tool: `old_snippet` must match the file's CURRENT
        contents exactly and uniquely, and gets replaced with `new_snippet`) —
        then gate the change behind two checks: the whole project must still
        compile, and every already-proved rule must still be provable by the
        real qed-prover. Either failure reverts the file automatically —
        this can never leave the shared DSL in a broken or regressed state,
        only ever "unchanged" or "changed and independently re-verified".

        Deliberately a snippet edit rather than a full-file replacement: these
        files run ~200+ lines, and asking for the whole file back on every
        edit means a single truncated response (e.g. the model's output
        budget got shrunk mid-conversation) silently corrupts it. A snippet
        edit costs only as many tokens as the actual change."""
        if file not in EXTENDABLE_DSL_FILES:
            return {"error": f"file must be one of {list(EXTENDABLE_DSL_FILES)}, got {file!r}"}
        path = self.rulescript_root / "src" / "main" / "java" / "org" / "qed" / file
        if not path.exists():
            return {"error": f"{file} not found at expected location"}
        original = path.read_text()

        count = original.count(old_snippet)
        if count == 0:
            return {
                "error": f"old_snippet not found verbatim in the current contents of {file}. "
                         "Re-read the file and copy the exact text you want to replace "
                         "(whitespace and all) — don't paraphrase it."
            }
        if count > 1:
            return {
                "error": f"old_snippet matches {count} places in {file}, not exactly one. "
                         "Include more surrounding context so it's unique."
            }
        new_content = original.replace(old_snippet, new_snippet, 1)

        path.write_text(new_content)
        compile_result = self.pipeline.compile()
        if not compile_result.ok:
            path.write_text(original)
            self.pipeline.compile()
            return {
                "ok": False, "stage": "compile", "reverted": True,
                "output": compile_result.output[:3000],
                "note": f"{file} reverted to its original contents.",
            }

        regressions = []
        checked = []
        for name in self.baseline_proved_rules:
            json_result, json_path = self.pipeline.generate_json(name, self.json_out_dir)
            if not json_result.ok or not json_path.exists():
                regressions.append({"rule": name, "stage": "json_generation", "output": json_result.output[:1000]})
                continue
            _, parsed = self.pipeline.run_prover(json_path)
            checked.append(name)
            if not (parsed and parsed.get("provable") is True):
                regressions.append({"rule": name, "stage": "prove", "result": parsed})

        if regressions:
            path.write_text(original)
            self.pipeline.compile()
            return {
                "ok": False, "stage": "regression", "reverted": True,
                "regressions": regressions,
                "note": f"{file} reverted: {len(regressions)}/{len(self.baseline_proved_rules)} "
                        "previously-proved rule(s) would have broken.",
            }

        self.dsl_edits.append({
            "file": file, "old_snippet": old_snippet, "new_snippet": new_snippet, "reason": reason,
        })
        return {
            "ok": True, "file": file, "reason": reason,
            "regression_checked": checked,
            "note": f"{file} extended and kept; {len(checked)} previously-proved rule(s) "
                    "re-verified with no regressions. Re-run try_rule to test your rule "
                    "against the extended DSL.",
        }


    def specs(self) -> list[dict]:
        def fn(name, description, properties, required):
            return {
                "name": name,
                "description": description,
                "parameters": {"type": "object", "properties": properties, "required": required},
            }

        roots = ["rulescript", "calcite"] + (["docs"] if self.docs_root is not None else [])
        root_enum = {"type": "string", "enum": roots}
        return [
            fn("list_directory",
               "List files and subdirectories at a path under one of the source roots "
               "('docs' is the reference-paper folder — rulescript.pdf, qed.pdf; read those "
               "with read_pdf, not read_file).",
               {"root": root_enum, "path": {"type": "string", "description": "Path relative to the root, e.g. 'src/main/java/org/qed' or 'core/src/main/java/org/apache/calcite/rel/rules'."}},
               ["root", "path"]),
            fn("search_code",
               "grep (extended regex) for a pattern across .java files under a root, optionally scoped to a subpath. Returns matching 'path:line:text' entries.",
               {"root": root_enum, "query": {"type": "string"}, "path": {"type": "string", "description": "Optional subpath to scope the search; defaults to the whole root."}},
               ["root", "query"]),
            fn("find_symbol",
               "Find where a Java class/interface/record/enum/method named `symbol` is declared or called, across a root.",
               {"root": root_enum, "symbol": {"type": "string"}},
               ["root", "symbol"]),
            fn("read_file",
               f"Read a plain-text file (or a line range of it — max {MAX_READ_LINES} lines "
               "per call) under a root. For PDFs under 'docs', use read_pdf instead.",
               {"root": root_enum, "path": {"type": "string"},
                "start_line": {"type": "integer", "description": "1-based, optional"},
                "end_line": {"type": "integer", "description": "1-based, optional"}},
               ["root", "path"]),
        ] + ([
            fn("read_pdf",
               f"Extract and page through text from a PDF under the 'docs' root (rulescript.pdf, "
               f"qed.pdf) via pdftotext — max {MAX_READ_LINES} lines per call, same pagination as "
               "read_file. The DSL reference already in your system prompt covers routine lookups; "
               "reach for this only when you need detail the papers cover that isn't in that reference.",
               {"path": {"type": "string", "description": "Path relative to the docs root, e.g. 'rulescript.pdf'."},
                "start_line": {"type": "integer", "description": "1-based, optional"},
                "end_line": {"type": "integer", "description": "1-based, optional"}},
               ["path"]),
        ] if self.docs_root is not None else []) + [
            fn("try_rule",
               "Write your current candidate RuleScript file, compile the project, "
               "serialize the rule to QED's JSON format, and run the real qed-prover "
               "on it. Returns exactly what failed (compile error / JSON generation "
               "error / QED's provable=true|false result with stats) so you can "
               "iterate. Call this as many times as you need.",
               {"java_source": {"type": "string", "description": "The complete .java file contents."}},
               ["java_source"]),
            fn("extend_dsl_file",
               "Rare / last resort: a find-and-replace edit (like a normal code editor — "
               "not a full-file rewrite) on one of RelRN.java, RexRN.java, or "
               "JSONSerializer.java, to add an operator the DSL doesn't expose yet, when "
               "your rule genuinely needs it. `old_snippet` must match the file's exact "
               "current text and occur exactly once; keep both snippets as small as the "
               "actual change needs (e.g. add one method/record, don't paste the whole "
               "file back) — the smaller the edit, the less likely a single truncated "
               "response corrupts it. Automatically reverted if the project fails to "
               "compile OR if any already-proved rule stops being provable afterward — "
               "you cannot silently break the shared DSL. Only use this after reading "
               "the target file in full and confirming the gap is real; prefer solving "
               "it within the existing API first.",
               {"file": {"type": "string", "enum": list(EXTENDABLE_DSL_FILES)},
                "old_snippet": {"type": "string", "description": "Exact text currently in the file to replace (must match uniquely)."},
                "new_snippet": {"type": "string", "description": "The replacement text."},
                "reason": {"type": "string", "description": "One sentence: what this adds and why it's needed."}},
               ["file", "old_snippet", "new_snippet", "reason"]),
        ]

    def call(self, name: str, arguments: dict) -> dict:
        method = getattr(self, name, None)
        if method is None:
            return {"error": f"unknown tool {name!r}"}
        try:
            return method(**arguments)
        except ToolError as e:
            return {"error": str(e)}
        except TypeError as e:
            return {"error": f"bad arguments for {name}: {e}"}
