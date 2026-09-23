"""Compile -> serialize-to-JSON -> run qed-prover, for a single RuleScript rule.

Everything here shells out to the existing Maven project and the qed-prover
binary; nothing about the QED prover itself is modified or reimplemented.
"""
from __future__ import annotations

import json
import re
import subprocess
import sys
from dataclasses import dataclass
from pathlib import Path

AGENT_DIR = Path(__file__).resolve().parent
SUPPORT_DIR = AGENT_DIR / "support"
CACHE_DIR = AGENT_DIR / ".cache"

SCOPE_RE = re.compile(r"^\s*//\s*SCOPE:\s*(FULL|PARTIAL)\s*(?:—|-)?\s*(.*)$", re.IGNORECASE | re.MULTILINE)


def extract_scope(java_source: str | None) -> tuple[str, str]:
    """Pull the required `// SCOPE: FULL` / `// SCOPE: PARTIAL — <reason>` tag
    out of a rule file. Returns (scope, detail); scope is "FULL", "PARTIAL", or
    "UNSPECIFIED" if the model didn't include the tag at all."""
    if not java_source:
        return "UNSPECIFIED", ""
    m = SCOPE_RE.search(java_source)
    if not m:
        return "UNSPECIFIED", ""
    return m.group(1).upper(), m.group(2).strip()


@dataclass
class CommandResult:
    ok: bool
    stdout: str
    stderr: str
    returncode: int

    @property
    def output(self) -> str:
        return (self.stdout + "\n" + self.stderr).strip()


def _run(cmd: list[str], cwd: Path, timeout: float = 300.0) -> CommandResult:
    try:
        proc = subprocess.run(
            cmd, cwd=str(cwd), capture_output=True, text=True, timeout=timeout
        )
        return CommandResult(proc.returncode == 0, proc.stdout, proc.stderr, proc.returncode)
    except subprocess.TimeoutExpired as e:
        return CommandResult(False, e.stdout or "", (e.stderr or "") + "\n[TIMED OUT]", -1)


class Pipeline:
    def __init__(self, repo_dir: Path, qed_prover_bin: Path, rules_out_dir: Path | None = None):
        self.repo_dir = repo_dir
        self.qed_prover_bin = qed_prover_bin
        self.rules_dir = repo_dir / "src/main/java/org/qed/RRuleInstances"
        self.unprovable_dir = repo_dir / "src/main/java/org/qed/UnprovableRRuleInstances"
        self.rules_out_dir = rules_out_dir or (AGENT_DIR / "rules")
        CACHE_DIR.mkdir(exist_ok=True)


    def rule_path(self, rule_name: str) -> Path:
        return self.rules_dir / f"{rule_name}.java"

    def write_rule(self, rule_name: str, java_source: str) -> Path:
        self.rules_dir.mkdir(parents=True, exist_ok=True)
        path = self.rule_path(rule_name)
        path.write_text(java_source)
        return path

    def remove_rule(self, rule_name: str) -> None:
        path = self.rule_path(rule_name)
        if path.exists():
            path.unlink()

    def stash_unprovable(self, rule_name: str, java_source: str, reason: str) -> Path:
        self.unprovable_dir.mkdir(parents=True, exist_ok=True)
        path = self.unprovable_dir / f"{rule_name}.java.rejected"
        header = (
            f"// NOT VERIFIED — QED could not prove this rule.\n"
            f"// Reason: {reason}\n"
            f"// Renamed to .java.rejected so it is excluded from the build;\n"
            f"// rename back to .java only after fixing the encoding.\n\n"
        )
        path.write_text(header + java_source)
        return path


    def publish(
        self,
        rule_name: str,
        java_source: str | None,
        *,
        status: str,
        spec_backend: str,
        spec_description: str,
        json_path: Path | None = None,
        result_json: dict | None = None,
        verifier_verdict: str = "",
        verifier_reasoning: str = "",
        attempts_used: int = 0,
        rounds_used: int = 0,
        scope: str = "",
        scope_detail: str = "",
    ) -> Path:
        """Mirror the final artifact for this rule into <root>/rules/<Name>/ so a
        human can see exactly what was produced without digging into the vendored
        Maven project — the *.java there is a copy for inspection, not the build
        input (that stays in the RRuleInstances tree while it's the active build)."""
        out = self.rules_out_dir / rule_name
        out.mkdir(parents=True, exist_ok=True)
        if java_source:
            (out / f"{rule_name}.java").write_text(java_source)
        if json_path and json_path.exists():
            (out / f"{rule_name}.json").write_text(json_path.read_text())
        if result_json is not None:
            (out / f"{rule_name}.result.json").write_text(json.dumps(result_json, indent=2))
        report = [
            f"# {rule_name}",
            "",
            f"**Status:** {status}" + (f"  **Scope:** {scope}" if scope else ""),
            f"**Source backend:** {spec_backend}",
            f"**Porter attempts used:** {attempts_used}  **Verification rounds used:** {rounds_used}",
            *([f"**Scope detail:** {scope_detail}", ""] if scope_detail else []),
            "",
            "## Source rule (as given to the porter)",
            "",
            "```",
            spec_description.strip(),
            "```",
            "",
        ]
        if verifier_verdict:
            report += [
                "## Independent verifier review",
                "",
                f"**Verdict:** {verifier_verdict}",
                "",
                verifier_reasoning or "(no reasoning recorded)",
                "",
            ]
        if result_json is not None:
            report += ["## QED prover result", "", "```json", json.dumps(result_json, indent=2), "```", ""]
        (out / "REPORT.md").write_text("\n".join(report))
        return out


    def compile(self) -> CommandResult:
        return _run(["./mvnw", "-q", "compile"], cwd=self.repo_dir, timeout=300)

    def _classpath_cache_file(self) -> Path:
        import hashlib
        key = hashlib.sha1(str(self.repo_dir.resolve()).encode()).hexdigest()[:12]
        return CACHE_DIR / f"classpath-{key}.txt"

    def ensure_classpath(self, force: bool = False) -> str:
        cache = self._classpath_cache_file()
        if not force and cache.exists():
            return cache.read_text().strip()
        out_file = CACHE_DIR / "cp_raw.txt"
        if out_file.exists():
            cp = f"{self.repo_dir / 'target/classes'}:{out_file.read_text().strip()}"
            cache.write_text(cp)
            return cp
        result = _run(
            [
                "./mvnw",
                "-q",
                "dependency:build-classpath",
                f"-Dmdep.outputFile={out_file}",
            ],
            cwd=self.repo_dir,
            timeout=300,
        )
        if not result.ok or not out_file.exists():
            raise RuntimeError(f"Failed to build classpath: {result.output}")
        cp = f"{self.repo_dir / 'target/classes'}:{out_file.read_text().strip()}"
        cache.write_text(cp)
        return cp

    def ensure_json_generator(self, classpath: str) -> Path:
        marker = CACHE_DIR / "JsonGenerator.class"
        if not marker.exists():
            result = _run(
                [
                    "javac",
                    "-cp",
                    classpath,
                    "-d",
                    str(CACHE_DIR),
                    str(SUPPORT_DIR / "JsonGenerator.java"),
                ],
                cwd=self.repo_dir,
                timeout=120,
            )
            if not result.ok:
                raise RuntimeError(f"Failed to compile JsonGenerator helper: {result.output}")
        return CACHE_DIR

    def generate_json(self, rule_name: str, out_dir: Path) -> tuple[CommandResult, Path]:
        classpath = self.ensure_classpath()
        generator_dir = self.ensure_json_generator(classpath)
        full_cp = f"{generator_dir}:{classpath}"
        out_dir.mkdir(parents=True, exist_ok=True)
        result = _run(
            [
                "java",
                "-cp",
                full_cp,
                "JsonGenerator",
                f"org.qed.RRuleInstances.{rule_name}",
                str(out_dir),
            ],
            cwd=self.repo_dir,
            timeout=120,
        )
        return result, out_dir / f"{rule_name}.json"


    def run_prover(self, json_path: Path, timeout: float = 90.0) -> tuple[CommandResult, dict | None]:
        result = _run([str(self.qed_prover_bin), str(json_path)], cwd=json_path.parent, timeout=timeout)
        result_path = json_path.with_suffix(".result")
        parsed = None
        if result_path.exists():
            try:
                parsed = json.loads(result_path.read_text())
            except json.JSONDecodeError:
                parsed = None
        return result, parsed
