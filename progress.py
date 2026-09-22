"""Tracks the summarizing progress document (PROGRESS.md + progress.json)."""
from __future__ import annotations

import json
from dataclasses import asdict, dataclass, field
from datetime import datetime, timezone
from pathlib import Path

STATUS_ORDER = ["PROVED", "FAILED", "SKIPPED"]
STATUS_EMOJI = {"PROVED": "✅", "FAILED": "❌", "SKIPPED": "⏭️"}


@dataclass
class RuleAttempt:
    rule_name: str
    source_backend: str
    source_description: str
    status: str  # PROVED | FAILED | SKIPPED
    attempts_used: int
    reason: str = ""
    prover_stats: dict = field(default_factory=dict)
    verification_rounds_used: int = 0
    verifier_verdict: str = ""  # CONFIRMED | AGREE | REJECTED-then-fixed | ...
    verifier_reasoning: str = ""
    scope: str = ""  # FULL | PARTIAL | UNSPECIFIED | "" (n/a for non-PROVED)
    scope_detail: str = ""
    timestamp: str = field(default_factory=lambda: datetime.now(timezone.utc).isoformat())


class ProgressLog:
    def __init__(self, json_path: Path, md_path: Path):
        self.json_path = json_path
        self.md_path = md_path
        self.entries: list[dict] = []
        if json_path.exists():
            self.entries = json.loads(json_path.read_text())

    def record(self, attempt: RuleAttempt) -> None:
        # Re-read from disk right before merging, not just once at construction —
        # a long-running process (e.g. many verification rounds on a hard rule)
        # can otherwise overwrite entries a concurrently-running process on a
        # different rule already saved in the meantime, silently losing them.
        # This narrows the race to the moment between this read and the write
        # below, rather than the entire lifetime of the process.
        if self.json_path.exists():
            try:
                self.entries = json.loads(self.json_path.read_text())
            except json.JSONDecodeError:
                pass  # keep in-memory entries if the file is mid-write elsewhere
        # Replace any prior entry for the same rule name (a rule may be re-run).
        self.entries = [e for e in self.entries if e["rule_name"] != attempt.rule_name]
        self.entries.append(asdict(attempt))
        self._save()

    def _save(self) -> None:
        self.json_path.write_text(json.dumps(self.entries, indent=2))
        self.md_path.write_text(self._render_markdown())

    def _render_markdown(self) -> str:
        by_status = {s: [e for e in self.entries if e["status"] == s] for s in STATUS_ORDER}
        total = len(self.entries)
        lines = [
            "# RuleScript porting progress",
            "",
            f"_Last updated: {datetime.now(timezone.utc).isoformat()}_",
            "",
            f"**{len(by_status['PROVED'])}/{total} rules proved** "
            f"({len(by_status['FAILED'])} failed, {len(by_status['SKIPPED'])} skipped as "
            "out of QED's supported fragment).",
            "",
            "| Rule | Backend | Status | Scope | Attempts | Notes |",
            "|---|---|---|---|---|---|",
        ]
        for e in sorted(self.entries, key=lambda e: (STATUS_ORDER.index(e["status"]), e["rule_name"])):
            emoji = STATUS_EMOJI.get(e["status"], "")
            scope = e.get("scope") or ("—" if e["status"] != "PROVED" else "UNSPECIFIED")
            note = e["reason"].replace("\n", " ").strip()
            if len(note) > 140:
                note = note[:137] + "..."
            lines.append(
                f"| `{e['rule_name']}` | {e['source_backend']} | {emoji} {e['status']} | {scope} "
                f"| {e['attempts_used']} | {note} |"
            )
        lines.append("")
        lines.append("## Details")
        lines.append("")
        for e in sorted(self.entries, key=lambda e: (STATUS_ORDER.index(e["status"]), e["rule_name"])):
            lines.append(f"### `{e['rule_name']}` — {STATUS_EMOJI.get(e['status'], '')} {e['status']}")
            lines.append("")
            lines.append(f"- Source backend: {e['source_backend']}")
            lines.append(f"- Source rule: {e['source_description']}")
            lines.append(f"- Attempts used: {e['attempts_used']}")
            lines.append(f"- Last updated: {e['timestamp']}")
            if e["reason"]:
                lines.append(f"- Reason / notes: {e['reason']}")
            if e["prover_stats"]:
                stats = e["prover_stats"]
                lines.append(
                    f"- QED stats: complete_fragment={stats.get('complete_fragment')}, "
                    f"total_duration={stats.get('total_duration')}, "
                    f"panicked={stats.get('panicked')}"
                )
            lines.append("")
        return "\n".join(lines) + "\n"
