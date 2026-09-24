from __future__ import annotations

import difflib
from pathlib import Path

from pipeline import Pipeline


def run_regression_audit(pipeline: Pipeline, rule_names: list[str], json_out_dir: Path) -> list[dict]:
    results = []
    for name in rule_names:
        json_result, json_path = pipeline.generate_json(name, json_out_dir)
        if not json_result.ok or not json_path.exists():
            results.append({
                "rule": name, "ok": False, "stage": "json_generation",
                "output": json_result.output[:1000],
            })
            continue
        _, parsed = pipeline.run_prover(json_path)
        ok = bool(parsed and parsed.get("provable") is True)
        results.append({"rule": name, "ok": ok, "stage": "prove", "result": parsed})
    return results


def unified_diff(old: str, new: str, filename: str) -> str:
    return "".join(difflib.unified_diff(
        old.splitlines(keepends=True), new.splitlines(keepends=True),
        fromfile=f"{filename} (before)", tofile=f"{filename} (after)",
    ))


def write_report(
    rules_out_dir: Path,
    triggering_rule: str,
    diffs: dict[str, str],
    reason: str,
    regression_results: list[dict],
    llm_verdict: str,
    llm_reasoning: str,
    final_outcome: str,
) -> Path:
    out_dir = rules_out_dir / "_dsl_audits"
    out_dir.mkdir(parents=True, exist_ok=True)
    path = out_dir / f"{triggering_rule}.md"
    lines = [
        f"# DSL extension audit — triggered by `{triggering_rule}`",
        "",
        f"**Files changed:** {', '.join(diffs) or '(none)'}",
        f"**Reason given by porter:** {reason}",
        f"**Final outcome:** {final_outcome}",
        "",
        "## Mechanical regression re-proof (fresh, real qed-prover)",
        "",
        f"Re-checked {len(regression_results)} rule(s) proved before this change.",
        "",
        "| Rule | Still provable? |",
        "|---|---|",
    ]
    for r in regression_results:
        status = "✅ yes" if r["ok"] else f"❌ NO — {r.get('stage', '')}"
        lines.append(f"| `{r['rule']}` | {status} |")
    lines += [
        "",
        "## Independent LLM review of the diff",
        "",
        f"**Verdict:** {llm_verdict}",
        "",
        llm_reasoning or "(no reasoning recorded)",
        "",
    ]
    for fname, diff_text in diffs.items():
        lines += [f"## Diff: `{fname}`", "", "```diff", diff_text.strip() or "(no textual diff)", "```", ""]
    path.write_text("\n".join(lines))
    return path
