from __future__ import annotations

import json
import re
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
    backend_root: Path
    pipeline: Pipeline
    rule_name: str
    json_out_dir: Path
    backend_name: str = "calcite"
    docs_root: Path | None = None
    local_root: Path | None = None
    baseline_proved_rules: tuple[str, ...] = ()
    dsl_edits: list = field(default_factory=list)

    def _scrub(self, text: str) -> str:
        if self.local_root is None or not text:
            return text
        return text.replace(str(self.local_root.resolve()), "<repo>").replace(str(self.local_root), "<repo>")

    def _root(self, root: str) -> Path:
        if root == "rulescript":
            return self.rulescript_root
        if root == self.backend_name:
            return self.backend_root
        if root == "docs" and self.docs_root is not None:
            return self.docs_root
        if root == "output":
            return self.json_out_dir
        raise ToolError(f"unknown root {root!r}, must be 'rulescript', {self.backend_name!r}, 'docs', or 'output'")


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

    def _java_root(self, root: str) -> Path:
        if root not in ("rulescript", self.backend_name):
            raise ToolError(
                f"root {root!r} is not a Java source tree — search_code/find_symbol only work "
                f"under 'rulescript' or {self.backend_name!r}. For the docs PDFs, use search_docs; "
                "for a rule's own generated JSON, use read_file with root='output'."
            )
        return self._root(root)

    def search_code(self, root: str, query: str, path: str = ".") -> dict:
        base = self._java_root(root)
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
        base = self._java_root(root)
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

    def _docs_md_cache_dir(self) -> Path:
        base = self.local_root if self.local_root is not None else Path(".")
        d = base / ".cache" / "docs_md"
        d.mkdir(parents=True, exist_ok=True)
        return d

    def _pdf_text(self, target: Path) -> list[str] | dict:
        cache_file = self._docs_md_cache_dir() / (target.stem + ".md")
        if cache_file.exists() and cache_file.stat().st_mtime >= target.stat().st_mtime:
            return cache_file.read_text().splitlines()
        try:
            result = subprocess.run(
                ["pdftotext", "-layout", str(target), "-"],
                capture_output=True, text=True, timeout=60,
            )
        except FileNotFoundError:
            return {"error": "pdftotext (poppler) is not installed on this host; cannot extract PDF text"}
        if result.returncode != 0:
            return {"error": f"pdftotext failed: {result.stderr[:1000]}"}
        cache_file.write_text(result.stdout)
        return result.stdout.splitlines()

    def read_pdf(self, path: str, start_line: int | None = None, end_line: int | None = None) -> dict:
        if self.docs_root is None:
            return {"error": "docs root not configured"}
        target = _resolve(self.docs_root, path)
        if not target.exists() or not target.is_file():
            return {"error": f"{path} is not a readable file under docs"}
        if target.suffix.lower() != ".pdf":
            return {"error": f"{path} is not a .pdf file; use read_file for plain text"}
        lines = self._pdf_text(target)
        if isinstance(lines, dict):
            return lines
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

    def search_docs(self, query: str, path: str | None = None) -> dict:
        if self.docs_root is None:
            return {"error": "docs root not configured"}
        if path:
            target = _resolve(self.docs_root, path)
            if target.suffix.lower() != ".pdf":
                return {"error": f"{path} is not a .pdf file"}
            pdfs = [target] if target.exists() else []
        else:
            pdfs = sorted(self.docs_root.glob("*.pdf"))
        try:
            pattern = re.compile(query, re.IGNORECASE)
        except re.error as e:
            return {"error": f"invalid regex {query!r}: {e}"}
        matches = []
        for pdf in pdfs:
            lines = self._pdf_text(pdf)
            if isinstance(lines, dict):
                continue
            for i, line in enumerate(lines, start=1):
                if pattern.search(line):
                    matches.append(f"{pdf.name}:{i}:{line.strip()}")
        truncated = len(matches) > MAX_SEARCH_RESULTS
        matches = matches[:MAX_SEARCH_RESULTS]
        return {
            "matches": matches, "truncated": truncated,
            "note": "call read_pdf(path=<name>, start_line=<near a match>) to see surrounding context"
                    if matches else "",
        }

    def list_ported_rules(self) -> dict:
        if self.local_root is None:
            return {"error": "project root not configured"}
        results = []
        for report in sorted(self.local_root.glob("*/rules/*/REPORT.md")):
            backend = report.parent.parent.parent.name
            rule = report.parent.name
            text = report.read_text()
            m = re.search(r"\*\*Status:\*\*\s*([A-Z]+)(?:\s+\*\*Scope:\*\*\s*(\S+))?", text)
            status = m.group(1) if m else "?"
            scope = m.group(2) if m and m.group(2) else ""
            results.append({"backend": backend, "rule": rule, "status": status, "scope": scope})
        return {"rules": results, "count": len(results)}

    def read_ported_rule(self, rule: str, backend: str | None = None) -> dict:
        if self.local_root is None:
            return {"error": "project root not configured"}
        matches = sorted(self.local_root.glob(f"{backend or '*'}/rules/{rule}/REPORT.md"))
        if not matches:
            scoped = f" under backend {backend!r}" if backend else ""
            return {"error": f"no ported rule named {rule!r} found{scoped} — call list_ported_rules to see what's available"}
        if len(matches) > 1:
            backends = [m.parent.parent.parent.name for m in matches]
            return {"error": f"rule {rule!r} exists under multiple backends {backends} — pass backend explicitly"}
        rule_dir = matches[0].parent
        java_path = rule_dir / f"{rule}.java"
        return {
            "backend": rule_dir.parent.parent.name,
            "report": matches[0].read_text(),
            "java": java_path.read_text() if java_path.exists() else None,
        }

    def list_rule_specs(self) -> dict:
        if self.local_root is None:
            return {"error": "project root not configured"}
        specs = []
        for pattern in ("*/rule_specs/*.md", "*/rule_specs/*.txt"):
            for spec_path in sorted(self.local_root.glob(pattern)):
                specs.append({"backend": spec_path.parent.parent.name, "name": spec_path.name})
        return {"specs": specs, "count": len(specs)}

    def read_rule_spec(self, name: str, backend: str | None = None) -> dict:
        if self.local_root is None:
            return {"error": "project root not configured"}
        matches = sorted(self.local_root.glob(f"{backend or '*'}/rule_specs/{name}"))
        if not matches:
            scoped = f" under backend {backend!r}" if backend else ""
            return {"error": f"no rule-spec file named {name!r} found{scoped} — call list_rule_specs to see what's available"}
        if len(matches) > 1:
            backends = [m.parent.parent.name for m in matches]
            return {"error": f"{name!r} exists under multiple backends {backends} — pass backend explicitly"}
        return {"backend": matches[0].parent.parent.name, "content": matches[0].read_text()}

    def try_rule(self, java_source: str) -> dict:
        self.pipeline.write_rule(self.rule_name, java_source)
        compile_result = self.pipeline.compile()
        if not compile_result.ok:
            return {"stage": "compile", "ok": False, "output": self._scrub(compile_result.output[:3000])}

        json_result, json_path = self.pipeline.generate_json(self.rule_name, self.json_out_dir)
        if not json_result.ok or not json_path.exists():
            return {"stage": "json_generation", "ok": False, "output": self._scrub(json_result.output[:3000])}

        prover_result, parsed = self.pipeline.run_prover(json_path)
        if parsed is None:
            return {"stage": "prove", "ok": False, "output": self._scrub(prover_result.output[:3000])}
        result = {"stage": "prove", "ok": True, "provable": parsed.get("provable"),
                  "result": parsed, "json_path": json_path.name}
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
                "output": self._scrub(compile_result.output[:3000]),
                "note": f"{file} reverted to its original contents.",
            }

        regressions = []
        checked = []
        for name in self.baseline_proved_rules:
            json_result, json_path = self.pipeline.generate_json(name, self.json_out_dir)
            if not json_result.ok or not json_path.exists():
                regressions.append({"rule": name, "stage": "json_generation", "output": self._scrub(json_result.output[:1000])})
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

        roots = ["rulescript", self.backend_name] + (["docs"] if self.docs_root is not None else []) + ["output"]
        root_enum = {"type": "string", "enum": roots}
        search_root_enum = {"type": "string", "enum": ["rulescript", self.backend_name]}
        return [
            fn("list_directory",
               "List files and subdirectories at a path under one of the roots. 'docs' is the "
               "reference-paper folder (rulescript.pdf, qed.pdf — read with read_pdf/search_docs, "
               "never read_file/search_code, since they're binary). 'output' is where THIS rule's "
               "own generated QED JSON lands after a try_rule call, for when you need to see exactly "
               "what got sent to the prover.",
               {"root": root_enum, "path": {"type": "string", "description": f"Path relative to the root, e.g. 'src/main/java/org/qed' for rulescript, or wherever {self.backend_name!r}'s own source layout puts a rule file."}},
               ["root", "path"]),
            fn("search_code",
               f"grep (extended regex) for a pattern across .java files under 'rulescript' or "
               f"{self.backend_name!r} — the only two Java source trees. Not valid for 'docs' (PDFs aren't "
               "greppable text; use search_docs) or 'output' (a single JSON file; use read_file). "
               "Returns matching 'path:line:text' entries.",
               {"root": search_root_enum, "query": {"type": "string"}, "path": {"type": "string", "description": "Optional subpath to scope the search; defaults to the whole root."}},
               ["root", "query"]),
            fn("find_symbol",
               f"Find where a Java class/interface/record/enum/method named `symbol` is declared or "
               f"called, under 'rulescript' or {self.backend_name!r} only (same restriction as search_code).",
               {"root": search_root_enum, "symbol": {"type": "string"}},
               ["root", "symbol"]),
            fn("read_file",
               f"Read a plain-text file (or a line range of it — max {MAX_READ_LINES} lines "
               "per call) under a root. For PDFs under 'docs', use read_pdf instead — this will "
               "return garbage on a PDF, not an error, since it doesn't know the file is binary.",
               {"root": root_enum, "path": {"type": "string"},
                "start_line": {"type": "integer", "description": "1-based, optional"},
                "end_line": {"type": "integer", "description": "1-based, optional"}},
               ["root", "path"]),
        ] + ([
            fn("read_pdf",
               f"Extract and page through text from a PDF under the 'docs' root (rulescript.pdf, "
               f"qed.pdf) via pdftotext, cached after the first read — max {MAX_READ_LINES} lines "
               "per call, same pagination as read_file. The DSL reference already in your system "
               "prompt covers routine lookups; reach for this only when you need detail the papers "
               "cover that isn't in that reference. To find WHERE in a PDF something is discussed "
               "before paging through it, use search_docs first.",
               {"path": {"type": "string", "description": "Path relative to the docs root, e.g. 'rulescript.pdf'."},
                "start_line": {"type": "integer", "description": "1-based, optional"},
                "end_line": {"type": "integer", "description": "1-based, optional"}},
               ["path"]),
            fn("search_docs",
               "grep (case-insensitive extended regex) across the docs PDFs' text — this is the "
               "'docs' equivalent of search_code, since search_code itself only works on Java source "
               "and cannot see into PDFs at all. Returns 'filename:line:text' entries whose line "
               "numbers match what read_pdf pages through, so you can jump straight to context with "
               "read_pdf(path=filename, start_line=<near a match>).",
               {"query": {"type": "string"},
                "path": {"type": "string", "description": "Optional: restrict to one PDF (e.g. 'qed.pdf'); defaults to searching all of them."}},
               ["query"]),
        ] if self.docs_root is not None else []) + ([
            fn("list_ported_rules",
               "List every rule already ported so far, across every backend this project has "
               "worked on (not just the current one) — name, backend, and outcome (PROVED/SKIPPED "
               "plus scope). Call this before starting a genuinely new rule: a structurally similar "
               "rule elsewhere often already found the right DSL pattern.",
               {}, []),
            fn("read_ported_rule",
               "Fetch a previously-ported rule's actual .java encoding (if it was proved) and its "
               "full reasoning/scope/QED-result writeup, by name. Call list_ported_rules first if "
               "you don't already know the exact name.",
               {"rule": {"type": "string"},
                "backend": {"type": "string", "description": "Only needed if the same rule name exists under more than one backend."}},
               ["rule"]),
            fn("list_rule_specs",
               "List every rule-spec input file across every backend this project has folders "
               "for, not just the one you're currently porting for — filename and backend.",
               {}, []),
            fn("read_rule_spec",
               "Read a rule-spec input file by exact filename (as list_rule_specs prints it), "
               "from any backend's rule_specs/ folder — e.g. to see how a similar rule was "
               "described for a different backend before it's even been attempted there.",
               {"name": {"type": "string", "description": "Exact filename, e.g. 'calcite_JoinCommute.md'."},
                "backend": {"type": "string", "description": "Only needed if the same filename exists under more than one backend."}},
               ["name"]),
        ] if self.local_root is not None else []) + [
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
