"""RuleScript porting agent.

Two agent roles collaborate on each rule:

  - the **porter**: given a pointer to a query-rewrite rule's source (in
    some external SQL backend — Apache Calcite, CockroachDB, DataFusion,
    ...), navigates two read-only source trees with tools (`list_directory`,
    `search_code`, `find_symbol`, `read_file`) to pull in exactly the code
    it needs — the source rule, the RuleScript DSL definitions — instead of
    everything being forced into one prompt regardless of the model's
    context budget. It iterates with a `try_rule` tool (compile -> JSON ->
    real qed-prover) on its own schedule until QED reports the rule
    provable, or it concludes the rule is unsupported.
  - the **verifier**: a fresh, independent LLM conversation (no shared
    history with the porter) that reviews the porter's final artifact —
    either a QED-proved encoding (checking it's a *faithful*, general
    rendering of the source rule) or an "unsupported" conclusion (checking
    it's genuinely outside what RuleScript/QED can express, not just the
    porter giving up early).

If the verifier rejects/disagrees, the porter gets another round (up to
--max-verification-rounds) with the verifier's critique folded into its
conversation.

The final outcome for each rule (PROVED / SKIPPED / FAILED), along with the
verifier's independently-checked reasoning, is recorded into PROGRESS.md,
and every artifact (whatever was produced) is mirrored under rules/<Name>/.

This script never modifies the QED prover itself — it only ever writes
files under vendor/rulescript-repo/src/main/java/org/qed/RRuleInstances/
(and, for candidates that never got proved, .../UnprovableRRuleInstances/,
kept only for human inspection).

Usage:

    # one rule, described in a spec file
    python3 port_rule.py --spec calcite/rule_specs/my_rule.md

    # a whole directory of spec files
    python3 port_rule.py --spec-dir calcite/rule_specs

    # smoke-test the compile/JSON/prove plumbing without calling any LLM,
    # by handing the porter's first tool call directly
    python3 port_rule.py --spec calcite/rule_specs/my_rule.md --offline-code my_rule.java

    # porting for a different backend: point spec-dir/progress/rules-out-dir
    # at that backend's own folder (created the same way calcite/ is laid out)
    python3 port_rule.py --spec-dir cockroach/rule_specs \
        --progress-md cockroach/PROGRESS.md --progress-json cockroach/progress.json \
        --rules-out-dir cockroach/rules

Configuration (env vars, or matching CLI flags):
    RULESCRIPT_AGENT_API_KEY / ANTHROPIC_API_KEY / OPENAI_API_KEY
    RULESCRIPT_AGENT_PROVIDER   ("anthropic" default, or "openai")
    RULESCRIPT_AGENT_MODEL
    RULESCRIPT_AGENT_ENDPOINT
"""
from __future__ import annotations

import argparse
import json
import os
import shutil
import subprocess
import sys
import time
from pathlib import Path

ROOT_DIR = Path(__file__).resolve().parent

sys.path.insert(0, str(ROOT_DIR))

import dsl_audit
import dsl_auditor_prompts
import prompts
import verifier_prompts
import workspace
from llm_client import (
    AgentTurn, LLMClient, LLMError, ToolCall,
    format_assistant_message, format_tool_results_message,
)

OFFLINE_PROVIDER = "openai"
from pipeline import Pipeline, extract_scope
from progress import ProgressLog, RuleAttempt
from pool import Pool
from repo_tools import EXTENDABLE_DSL_FILES, RepoTools
from spec import RuleSpec, parse_spec_file


class PorterResult:
    def __init__(self, outcome: str, turns_used: int, code: str | None = None,
                 prover_json: dict | None = None, json_path: Path | None = None,
                 reason: str = "", transcript_tail: str = "", round_log: list[str] | None = None):
        self.outcome = outcome
        self.turns_used = turns_used
        self.code = code
        self.prover_json = prover_json
        self.json_path = json_path
        self.reason = reason
        self.transcript_tail = transcript_tail
        self.round_log = round_log or []


def porter_agent_loop(
    spec: RuleSpec,
    llm: LLMClient | None,
    tools_obj: RepoTools,
    system: str,
    conversation: list[dict],
    max_turns: int,
    offline_code: str | None,
    debug_dir: Path | None = None,
) -> PorterResult:
    tool_specs = tools_obj.specs()
    last_reason = ""
    last_tried_code: str | None = None
    round_log: list[str] = []
    consecutive_empty_turns = 0

    for turn in range(1, max_turns + 1):
        print(f"-- porter turn {turn}/{max_turns}")

        if offline_code is not None and turn == 1 and len(conversation) == 1:
            agent_turn = AgentTurn(
                content="", tool_calls=[ToolCall(id="offline-1", name="try_rule", arguments={"java_source": offline_code})]
            )
        else:
            if llm is None:
                raise RuntimeError("No LLM configured and no --offline-code given.")
            try:
                agent_turn = llm.step(system, conversation, tool_specs)
            except LLMError as e:
                print(f"   LLM call failed: {e}")
                last_reason = f"LLM error: {e}"
                break

        if debug_dir is not None:
            calls_desc = "; ".join(f"{tc.name}({_redact(tc.arguments)})" for tc in agent_turn.tool_calls)
            dump_transcript_entry(debug_dir, spec.name, turn, "reply", f"{agent_turn.content}\n[tool_calls] {calls_desc}")
        if agent_turn.content:
            round_log.append(f"[turn {turn}] porter said: {agent_turn.content[:1500]}")
        provider = llm.provider if llm is not None else OFFLINE_PROVIDER
        conversation.append(format_assistant_message(provider, agent_turn))

        if not agent_turn.tool_calls:
            stripped = agent_turn.content.strip()
            if stripped.upper().startswith("UNSUPPORTED"):
                reason = stripped.split(":", 1)[1].strip() if ":" in stripped else stripped
                print(f"   porter declared UNSUPPORTED: {reason}")
                return PorterResult("UNSUPPORTED", turn, code=last_tried_code, reason=reason,
                                     transcript_tail=transcript_tail(conversation), round_log=round_log)
            consecutive_empty_turns += 1
            if consecutive_empty_turns >= 3:
                print(f"   plain-text reply with no tool call ({consecutive_empty_turns} in a row); nudging hard")
                conversation.append({"role": "user", "content": prompts.nudge_stop_deliberating()})
            else:
                print("   plain-text reply with no tool call; nudging")
                conversation.append({"role": "user", "content": prompts.nudge_use_tool_or_conclude()})
            last_reason = "model replied without calling a tool or declaring UNSUPPORTED"
            continue

        consecutive_empty_turns = 0
        results: list[tuple[ToolCall, dict]] = []
        proved: tuple[str, dict] | None = None
        for tc in agent_turn.tool_calls:
            print(f"   tool: {tc.name}({_redact(tc.arguments)})")
            result = tools_obj.call(tc.name, tc.arguments)
            results.append((tc, result))
            round_log.append(f"[turn {turn}] {tc.name}({_redact(tc.arguments)}) -> {_compact_result(result)}")
            if tc.name == "try_rule":
                last_tried_code = tc.arguments.get("java_source")
                print(f"      -> {result.get('stage')}: ok={result.get('ok')} provable={result.get('provable')}")
                if result.get("ok") and result.get("provable") is True:
                    scope, _ = extract_scope(last_tried_code)
                    if scope == "UNSPECIFIED":
                        result["provable"] = "true, but rejected pending fix"
                        result["error"] = (
                            "QED proved this, but the file is missing the required first "
                            "line `// SCOPE: FULL` or `// SCOPE: PARTIAL — <condition>`. "
                            "Add it and call try_rule again with the same code."
                        )
                        print("      -> missing required SCOPE tag, sending back for one more turn")
                    else:
                        proved = (last_tried_code, result)

        if proved is not None:
            code, result = proved
            json_path = Path(result["json_path"]) if result.get("json_path") else None
            print("   PROVED by qed-prover (pending independent verification)")
            return PorterResult("PROVED", turn, code=code, prover_json=result["result"], json_path=json_path,
                                 round_log=round_log)

        tr_msg = format_tool_results_message(provider, results)
        if tr_msg["role"] == "__multi_tool__":
            conversation.extend(tr_msg["messages"])
        else:
            conversation.append(tr_msg)

    return PorterResult(
        "EXHAUSTED", max_turns, code=last_tried_code,
        reason=last_reason or "exhausted turn budget without a proof or UNSUPPORTED conclusion",
        transcript_tail=transcript_tail(conversation), round_log=round_log,
    )


def _redact(arguments: dict) -> dict:
    return {k: ("<java source elided>" if k == "java_source" else v) for k, v in arguments.items()}


def _compact_result(result: dict, limit: int = 6000) -> str:
    text = json.dumps(result, ensure_ascii=False)
    if len(text) > limit:
        return text[:limit] + f"... [truncated, {len(text) - limit} more chars]"
    return text


def dump_transcript_entry(debug_dir: Path, rule_name: str, turn: int, kind: str, content: str) -> None:
    d = debug_dir / rule_name
    d.mkdir(parents=True, exist_ok=True)
    (d / f"turn_{turn:02d}_{kind}.txt").write_text(content)


def clear_reply_transcripts(debug_dir: Path, rule_name: str) -> None:
    d = debug_dir / rule_name
    if not d.exists():
        return
    for f in d.glob("turn_*_reply.txt"):
        f.unlink()


def clear_all_transcripts(debug_dir: Path, rule_name: str) -> None:
    d = debug_dir / rule_name
    if d.exists():
        shutil.rmtree(d, ignore_errors=True)


def transcript_tail(conversation: list[dict], n: int = 6) -> str:
    def render(m: dict) -> str:
        role = m.get("role", "?")
        content = m.get("content")
        if isinstance(content, str):
            return f"[{role}]\n{content[:1200]}"
        if isinstance(content, list):
            pieces = []
            for block in content:
                if block.get("type") == "text":
                    pieces.append(block["text"])
                elif block.get("type") == "tool_use":
                    pieces.append(f"<tool_use {block['name']}({_redact(block.get('input', {}))})>")
                elif block.get("type") == "tool_result":
                    pieces.append(f"<tool_result {str(block.get('content'))[:500]}>")
            return f"[{role}]\n" + "\n".join(pieces)[:1200]
        if m.get("role") == "assistant" and m.get("tool_calls"):
            calls = "; ".join(f"{tc['function']['name']}(...)" for tc in m["tool_calls"])
            return f"[assistant tool_calls] {calls}"
        return f"[{role}] (tool result)"

    return "\n\n".join(render(m) for m in conversation[-n:])


NO_VERIFIER = "NO_VERIFIER"


def _looks_like_reasoning_dump(reply: str) -> bool:
    lowered = reply.lower()
    if "<what you think" in lowered or "<1-3 sentence" in lowered or "<one-sentence" in lowered:
        return True
    return len(reply) > 4000


def run_verifier(
    verifier_llm: LLMClient | None, repo_dir: Path, spec: RuleSpec, kind: str,
    debug_dir: Path | None = None, **kwargs,
) -> tuple[str, str]:
    if verifier_llm is None:
        return NO_VERIFIER, "(no verifier LLM configured — result not independently reviewed)"
    system = verifier_prompts.system_prompt(repo_dir)
    if kind == "proved":
        prompt = verifier_prompts.review_proved(
            spec.name, spec.backend, kwargs["source_text"], kwargs["code"], kwargs["prover_json"]
        )
    else:
        prompt = verifier_prompts.review_unsupported(
            spec.name, spec.backend, kwargs["source_text"], kwargs["claimed_reason"], kwargs["transcript_tail"]
        )
    messages = [{"role": "user", "content": prompt}]
    reply = verifier_llm.complete(system, messages)
    verdict, reasoning = verifier_prompts.parse_verdict(reply)
    if debug_dir is not None:
        dump_transcript_entry(debug_dir, spec.name, 0, f"verifier_{kind}_1", reply)

    if verdict is None or _looks_like_reasoning_dump(reply):
        if verdict is None:
            nudge = (
                "Your reply didn't start with a parseable `VERDICT: <TOKEN>` line. "
                "Reply again with **only** the exact two-line format requested "
                "(`VERDICT: ...` then `REASONING: ...`), nothing else."
            )
        else:
            print(f"   [verifier] reply parsed to {verdict} but looks like an unfinished "
                  f"scratchpad ({len(reply)} chars) rather than a considered answer; retrying")
            nudge = (
                "That read like unfinished scratch thinking rather than your actual final "
                "answer — it's much longer than the 1-3 sentences requested, and/or echoes "
                "the format instructions themselves instead of filling them in. Work through "
                "your reasoning fully first, THEN reply again with **only** the exact "
                "two-line format (`VERDICT: ...` then `REASONING: ...` in 1-3 sentences), "
                "reflecting your actual final conclusion, not a first guess."
            )
        messages.append({"role": "assistant", "content": reply})
        messages.append({"role": "user", "content": nudge})
        reply2 = verifier_llm.complete(system, messages)
        verdict2, reasoning2 = verifier_prompts.parse_verdict(reply2)
        if debug_dir is not None:
            dump_transcript_entry(debug_dir, spec.name, 0, f"verifier_{kind}_2", reply2)
        if verdict2 is None or _looks_like_reasoning_dump(reply2):
            conservative = "REJECTED" if kind == "proved" else "DISAGREE"
            print(f"   [verifier] reply unusable twice, defaulting conservatively to {conservative}")
            return conservative, (
                f"(verifier's response could not be parsed into a considered answer after a "
                f"retry, so this was conservatively treated as {conservative} rather than "
                f"silently accepted; raw reply started with: {reply2.strip()[:300]!r})"
            )
        verdict, reasoning = verdict2, reasoning2
    return verdict, reasoning


def snapshot_dsl_files(rulescript_root: Path) -> dict[str, str]:
    base = rulescript_root / "src" / "main" / "java" / "org" / "qed"
    return {f: (base / f).read_text() for f in EXTENDABLE_DSL_FILES if (base / f).exists()}


def merge_rule_to_main(
    pipeline: Pipeline, rulescript_root: Path,
    rule_name: str, final_code: str, dsl_edits: list[dict],
    auditor_llm: LLMClient | None, progress_json_path: Path, lock_path: Path,
) -> tuple[str, str]:
    base = rulescript_root / "src" / "main" / "java" / "org" / "qed"
    with workspace.merge_lock(lock_path):
        pre_merge = {f: (base / f).read_text() for f in EXTENDABLE_DSL_FILES if (base / f).exists()}

        def restore_dsl() -> None:
            for f, content in pre_merge.items():
                (base / f).write_text(content)

        applied_files: list[str] = []
        for edit in dsl_edits:
            f = edit["file"]
            path = base / f
            current = path.read_text()
            if current.count(edit["old_snippet"]) != 1:
                restore_dsl()
                pipeline.compile()
                return "merge-conflict", (
                    f"{f}: this rule's recorded edit no longer applies cleanly to the current "
                    "shared DSL (another rule's merge likely already changed that region since "
                    "this workspace was forked). Needs a fresh attempt against the current DSL."
                )
            path.write_text(current.replace(edit["old_snippet"], edit["new_snippet"], 1))
            applied_files.append(f)

        regression_results: list[dict] = []
        if dsl_edits:
            compile_result = pipeline.compile()
            if not compile_result.ok:
                restore_dsl()
                pipeline.compile()
                return "merge-compile-failed", compile_result.output[:2000]

            current_baseline = ()
            if progress_json_path.exists():
                current_baseline = tuple(
                    e["rule_name"] for e in json.loads(progress_json_path.read_text())
                    if e["status"] == "PROVED" and e["rule_name"] != rule_name
                )
            regression_results = dsl_audit.run_regression_audit(
                pipeline, list(current_baseline), ROOT_DIR / ".cache" / "tmp-rules"
            )
            regressions = [r for r in regression_results if not r["ok"]]
            if regressions:
                restore_dsl()
                pipeline.compile()
                return "merge-regressed", (
                    f"{len(regressions)} previously-proved rule(s) would break: "
                    + ", ".join(r["rule"] for r in regressions)
                )

        pipeline.write_rule(rule_name, final_code)
        rule_compile = pipeline.compile()
        ok = rule_compile.ok
        json_path = None
        if ok:
            json_result, json_path = pipeline.generate_json(rule_name, ROOT_DIR / ".cache" / "tmp-rules")
            ok = json_result.ok and json_path.exists()
        if ok:
            _, parsed = pipeline.run_prover(json_path)
            ok = bool(parsed and parsed.get("provable") is True)
        if not ok:
            pipeline.remove_rule(rule_name)
            if dsl_edits:
                restore_dsl()
            pipeline.compile()
            return "merge-reproof-failed", (
                "The rule no longer re-proves against the current shared repo state — it may "
                "have diverged from what this rule's isolated workspace assumed."
            )

        if dsl_edits:
            diffs = {f: dsl_audit.unified_diff(pre_merge[f], (base / f).read_text(), f) for f in applied_files}
            reason = "; ".join(e["reason"] for e in dsl_edits)
            llm_verdict, llm_reasoning = "NO_AUDITOR", "(no auditor LLM configured — diff not independently reviewed)"
            if auditor_llm is not None:
                system = dsl_auditor_prompts.system_prompt()
                prompt = "\n\n".join(
                    dsl_auditor_prompts.review_dsl_change(f, diffs[f], reason, rule_name) for f in applied_files
                )
                try:
                    reply = auditor_llm.complete(system, [{"role": "user", "content": prompt}])
                    llm_verdict, llm_reasoning = verifier_prompts.parse_verdict(reply)
                    if llm_verdict is None:
                        llm_verdict = "UNSAFE"
                        llm_reasoning = f"(auditor reply unparseable, conservatively rejecting: {reply[:300]!r})"
                except LLMError as e:
                    llm_verdict, llm_reasoning = "UNSAFE", f"(auditor LLM call failed, conservatively rejecting: {e})"

            if llm_verdict == "UNSAFE":
                pipeline.remove_rule(rule_name)
                restore_dsl()
                pipeline.compile()
                dsl_audit.write_report(
                    pipeline.rules_out_dir, rule_name, diffs, reason, regression_results,
                    llm_verdict, llm_reasoning, "reverted (auditor: UNSAFE)",
                )
                return "merge-audit-failed", llm_reasoning

            dsl_audit.write_report(
                pipeline.rules_out_dir, rule_name, diffs, reason, regression_results,
                llm_verdict, llm_reasoning, "merged",
            )
            print(f"   [dsl] merged {applied_files}: {len(regression_results)} prior rule(s) "
                  f"re-verified fresh, auditor={llm_verdict}")

        return "merged", "ok"


SUMMARIZER_ROLE_PREAMBLE = """You are a careful technical note-taker working alongside a separate agent (the
"porter") that is trying to encode a formal query-plan rewrite rule in the
DSL described below. The porter just spent a round exploring and hit its
own context limit — it can't keep going in that same conversation. You're
being given a fresh, empty context specifically to read through everything
it did this round and extract what's worth handing back to it for its next
attempt. Below this preamble is the same DSL/task reference material the
porter itself was given, so you can judge what's actually new/useful
versus what it already knew going in — use it as background, but don't
guess at anything the round's own log (given after it) doesn't show."""

SUMMARY_REQUEST = """Below is the full log of the round that just ended: every tool call the porter
made and the (possibly truncated) result it got back, plus anything it
said along the way. If a summary from an earlier round is included above,
merge forward from it — carry over anything still true and useful, drop
anything this round's log has since superseded or corrected, and fold in
what's new. The porter will NOT see the raw log again — only the merged
summary you write now — so the result should be a single, self-contained,
up-to-date set of notes, not just an account of this one round in
isolation.

Reply with **only** a concise markdown summary (well under 500 words), no
other commentary:

- The exact DSL API confirmed by reading the files (method signatures,
  record shapes) — only things actually shown in the log(s), not guesses.
- Every encoding tried so far and exactly why each failed (compile error
  text, "not provable" + the hint given, a truncated/invalid
  extend_dsl_file edit, a context-length crash, etc.) — specific enough
  that the porter won't blindly repeat a mistake it already made.
- The most promising direction based on everything so far, if one is
  apparent.

Do not restate the rule's description or the DSL reference material above —
the next attempt already has those. If the independent reviewer's verdict
on this round's output is included below, use it to make sure your
"most promising direction" doesn't just point back at something the
reviewer already rejected — but do NOT quote or restate the reviewer's
wording itself in your summary; it's added back separately by the harness,
so repeating it here would just make it accumulate across rounds. The same
goes for anything the log shows an *earlier* round's reviewer already said
— summarize what it means for what to try next, don't reproduce the text."""


def latest_existing_summary(debug_dir: Path, rule_name: str) -> str:
    d = debug_dir / rule_name
    if not d.exists():
        return ""
    files = sorted(d.glob("round_*_summary.md"))
    return files[-1].read_text() if files else ""


def summarize_round(
    llm: LLMClient | None, system: str, round_log: list[str], verifier_feedback: str, debug_dir: Path,
    rule_name: str, round_num: int,
) -> str:
    if llm is None or not round_log:
        return ""
    try:
        summarizer_system = SUMMARIZER_ROLE_PREAMBLE + "\n\n" + system
        prev_summary = latest_existing_summary(debug_dir, rule_name)
        parts = []
        if prev_summary:
            parts.append(f"### Summary carried over from an earlier round\n\n{prev_summary}")
        log_text = "\n\n".join(round_log)
        parts.append(f"### This round's activity log\n\n{log_text}")
        if verifier_feedback:
            parts.append(
                "### The independent reviewer's verdict on this round's output\n\n"
                f"{verifier_feedback}"
            )
        parts.append(SUMMARY_REQUEST)
        prompt = "\n\n".join(parts)
        summary = llm.complete(summarizer_system, [{"role": "user", "content": prompt}])
    except LLMError as e:
        print(f"   [summary] couldn't summarize round {round_num}: {e}")
        return ""
    summary = summary.strip()
    if summary:
        path = debug_dir / rule_name / f"round_{round_num:02d}_summary.md"
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(summary)
        print(f"   [summary] wrote {path.relative_to(ROOT_DIR)}")
    return summary


def round_continuation_prompt(
    spec: RuleSpec, source_hint: str, last_code: str | None, feedback_header: str, reasoning: str,
    self_summary: str = "", backend_name: str = "calcite",
) -> str:
    parts = [
        prompts.initial_user_prompt(spec.name, spec.backend, spec.source_path, source_hint, backend_name),
        f"{feedback_header}\n\n{reasoning}",
    ]
    if self_summary:
        parts.append(
            "### Your own notes from last round's exploration\n\n"
            "These are things YOU already confirmed by reading the actual files last round — "
            "trust them and build on them directly. Do not re-read a file or re-derive a fact "
            "that's already stated below just to double-check it; that's what burned last "
            "round's turns without ever reaching `try_rule`. Only go back to a tool if you hit "
            "something these notes don't cover.\n\n" + self_summary
        )
    if last_code:
        parts.append(
            "Your most recent candidate, for reference — **assume this exact file was NOT "
            "accepted** (QED either didn't prove it, or a reviewer rejected it; see the "
            "feedback above for which and why). Don't call `try_rule` again with this same "
            "code unchanged just to re-confirm that verdict — that's a wasted turn, since "
            "the result won't change. Make a genuine, specific change based on the feedback "
            f"first (revise it if the fix is targeted; start over if it isn't), then test the "
            f"new version:\n\n```java\n{last_code}\n```"
        )
    parts.append(
        "Use your tools again as needed, then call `try_rule` with a "
        "corrected candidate — or reply with exactly `UNSUPPORTED: <reason>` "
        "only if you're now confident it's genuinely unsupported."
    )
    return "\n\n".join(parts)


def read_source_text(backend_root: Path, source_path: str) -> str:
    if not source_path:
        return "(no source path given)"
    p = backend_root / source_path
    if not p.exists():
        return f"(source path {source_path} not found under {backend_root})"
    return p.read_text()


def run_one(
    spec: RuleSpec,
    pipeline: Pipeline,
    llm: LLMClient | None,
    verifier_llm: LLMClient | None,
    rulescript_root: Path,
    backend_root: Path,
    max_turns: int,
    max_rounds: int,
    offline_code: str | None = None,
    auditor_llm: LLMClient | None = None,
    workspaces_dir: Path | None = None,
    merge_lock_path: Path | None = None,
    progress_json_path: Path | None = None,
    backend_name: str = "calcite",
) -> RuleAttempt:
    print(f"\n=== Porting {spec.name} (from {spec.backend}) ===")
    system = prompts.system_prompt()
    source_hint = spec.hint or "(no additional notes — read the source file for everything you need.)"
    conversation: list[dict] = [
        {"role": "user", "content": prompts.initial_user_prompt(spec.name, spec.backend, spec.source_path, source_hint, backend_name)}
    ]
    source_text = read_source_text(backend_root, spec.source_path)

    isolated = workspaces_dir is not None
    if isolated:
        with workspace.merge_lock(merge_lock_path):
            workspace_root = workspace.make_workspace(rulescript_root, workspaces_dir, spec.name)
        worker_pipeline = Pipeline(workspace_root, pipeline.qed_prover_bin, rules_out_dir=pipeline.rules_out_dir)
    else:
        workspace_root = rulescript_root
        worker_pipeline = pipeline

    baseline_proved = tuple(
        p.stem for p in worker_pipeline.rules_dir.glob("*.java") if p.stem != spec.name
    )
    tools_obj = RepoTools(
        workspace_root, backend_root, worker_pipeline, spec.name, ROOT_DIR / ".cache" / "tmp-rules",
        backend_name=backend_name,
        docs_root=ROOT_DIR / "docs",
        local_root=ROOT_DIR,
        baseline_proved_rules=baseline_proved,
    )
    dsl_snapshot = snapshot_dsl_files(workspace_root)
    debug_dir = ROOT_DIR / ".cache" / "transcripts"

    def cleanup() -> None:
        if isolated:
            workspace.cleanup_workspace(workspace_root)

    total_turns = 0
    last_reason = ""
    last_stats: dict = {}
    last_verifier_verdict = ""
    last_verifier_reasoning = ""

    for round_num in range(1, max_rounds + 1):
        print(f"--- verification round {round_num}/{max_rounds} ---")
        clear_reply_transcripts(debug_dir, spec.name)
        result = porter_agent_loop(spec, llm, tools_obj, system, conversation, max_turns, offline_code, debug_dir)
        total_turns += result.turns_used
        last_reason = result.reason
        last_stats = result.prover_json or {}

        if result.outcome == "PROVED":
            verdict, reasoning = run_verifier(
                verifier_llm, rulescript_root, spec, "proved", debug_dir,
                source_text=source_text, code=result.code, prover_json=result.prover_json,
            )
            last_verifier_verdict, last_verifier_reasoning = verdict, reasoning
            if verdict in (NO_VERIFIER, "CONFIRMED"):
                if isolated:
                    merge_status, merge_detail = merge_rule_to_main(
                        pipeline, rulescript_root, spec.name, result.code, tools_obj.dsl_edits,
                        auditor_llm, progress_json_path, merge_lock_path,
                    )
                else:
                    pipeline.write_rule(spec.name, result.code)
                    merge_status, merge_detail = "merged", "ok"
                if merge_status != "merged":
                    print(f"   [merge] failed ({merge_status}): {merge_detail}")
                    cleanup()
                    return RuleAttempt(
                        spec.name, spec.backend, spec.description, "FAILED", total_turns,
                        reason=f"Proved in an isolated workspace but failed to merge into the shared "
                               f"repo ({merge_status}): {merge_detail}",
                        prover_stats=result.prover_json, verification_rounds_used=round_num,
                        verifier_verdict=verdict, verifier_reasoning=reasoning,
                    )
                scope, scope_detail = extract_scope(result.code)
                final_reasoning = reasoning + (
                    "\n\n(This proof relies on an accepted RuleScript DSL extension made during "
                    "this session — see the extended file(s) for what changed.)" if tools_obj.dsl_edits else ""
                )
                pipeline.publish(
                    spec.name, result.code, status="PROVED", spec_backend=spec.backend,
                    spec_description=spec.description, json_path=result.json_path,
                    result_json=result.prover_json, verifier_verdict=verdict,
                    verifier_reasoning=final_reasoning, attempts_used=total_turns, rounds_used=round_num,
                    scope=scope, scope_detail=scope_detail,
                )
                print(f"   verifier {verdict}: merged into shared repo [SCOPE: {scope}]")
                print(f"   published for inspection under {pipeline.rules_out_dir.relative_to(ROOT_DIR)}/{spec.name}/")
                clear_all_transcripts(debug_dir, spec.name)
                cleanup()
                return RuleAttempt(
                    spec.name, spec.backend, spec.description, "PROVED", total_turns,
                    reason=final_reasoning, prover_stats=result.prover_json,
                    verification_rounds_used=round_num, verifier_verdict=verdict,
                    verifier_reasoning=final_reasoning, scope=scope, scope_detail=scope_detail,
                )
            print(f"   verifier {verdict}: {reasoning}")
            worker_pipeline.remove_rule(spec.name)
            self_summary = summarize_round(
                llm, system, result.round_log,
                f"REJECTED — the proof was not accepted as faithful: {reasoning}",
                debug_dir, spec.name, round_num,
            )
            conversation = [{
                "role": "user",
                "content": round_continuation_prompt(
                    spec, source_hint, result.code,
                    "An independent reviewer examined your proof and found it unfaithful "
                    "to the source rule:", reasoning, self_summary, backend_name,
                ),
            }]
            continue

        claimed_reason = result.reason or "porter exhausted its turn budget without a proof"
        verdict, reasoning = run_verifier(
            verifier_llm, rulescript_root, spec, "unsupported", debug_dir,
            source_text=source_text, claimed_reason=claimed_reason, transcript_tail=result.transcript_tail,
        )
        last_verifier_verdict, last_verifier_reasoning = verdict, reasoning
        if verdict in (NO_VERIFIER, "AGREE"):
            print(f"   verifier {verdict}: finalizing as SKIPPED — {reasoning}")
            worker_pipeline.remove_rule(spec.name)
            if result.code:
                pipeline.stash_unprovable(spec.name, result.code, reasoning)
            pipeline.publish(
                spec.name, result.code, status="SKIPPED", spec_backend=spec.backend,
                spec_description=spec.description, json_path=result.json_path,
                result_json=last_stats or None, verifier_verdict=verdict,
                verifier_reasoning=reasoning, attempts_used=total_turns, rounds_used=round_num,
            )
            print(f"   published (reasoning{'' if result.code else ' only, no candidate code'}) under {pipeline.rules_out_dir.relative_to(ROOT_DIR)}/{spec.name}/")
            clear_all_transcripts(debug_dir, spec.name)
            cleanup()
            return RuleAttempt(
                spec.name, spec.backend, spec.description, "SKIPPED", total_turns,
                reason=reasoning, prover_stats=last_stats,
                verification_rounds_used=round_num, verifier_verdict=verdict,
                verifier_reasoning=reasoning,
            )
        print(f"   verifier {verdict}: {reasoning}")
        self_summary = summarize_round(
            llm, system, result.round_log,
            f"DISAGREE — the reviewer believes this rule IS expressible and the round's own "
            f"conclusion was wrong: {reasoning}",
            debug_dir, spec.name, round_num,
        )
        conversation = [{
            "role": "user",
            "content": round_continuation_prompt(
                spec, source_hint, result.code,
                "An independent reviewer looked at your attempt and believes this rule "
                "IS expressible in RuleScript:", reasoning, self_summary, backend_name,
            ),
        }]

    print("   exhausted all verification rounds; marking FAILED for human follow-up")
    worker_pipeline.remove_rule(spec.name)
    last_code = result.code
    if last_code:
        pipeline.stash_unprovable(spec.name, last_code, last_reason)
    pipeline.publish(
        spec.name, last_code, status="FAILED", spec_backend=spec.backend,
        spec_description=spec.description, result_json=last_stats or None,
        verifier_verdict=last_verifier_verdict, verifier_reasoning=last_verifier_reasoning,
        attempts_used=total_turns, rounds_used=max_rounds,
    )
    print(f"   published (reasoning{'' if last_code else ' only, no candidate code'}) under {pipeline.rules_out_dir.relative_to(ROOT_DIR)}/{spec.name}/")
    cleanup()
    return RuleAttempt(
        spec.name, spec.backend, spec.description, "FAILED", total_turns,
        reason=last_reason, prover_stats=last_stats,
        verification_rounds_used=max_rounds, verifier_verdict=last_verifier_verdict,
        verifier_reasoning=last_verifier_reasoning,
    )


def build_llm(provider, model, api_key, endpoint, max_tokens, timeout) -> LLMClient:
    return LLMClient(
        provider=provider or os.environ.get("RULESCRIPT_AGENT_PROVIDER", "anthropic"),
        model=model or os.environ.get("RULESCRIPT_AGENT_MODEL"),
        api_key=api_key,
        endpoint=endpoint or os.environ.get("RULESCRIPT_AGENT_ENDPOINT"),
        max_tokens=max_tokens,
        timeout=timeout,
    )


def run_pool_worker(
    pool: Pool, worker_id: str, specs_by_name: dict[str, RuleSpec],
    pipeline: Pipeline, llm, verifier_llm, auditor_llm, args, progress: ProgressLog,
) -> None:
    while True:
        name = pool.claim_next(worker_id)
        if name is None:
            return
        spec = specs_by_name[name]
        print(f"\n[{worker_id}] claimed {name}")
        start = time.time()
        try:
            attempt = run_one(
                spec, pipeline, llm, verifier_llm, args.repo, args.backend_root,
                args.max_turns, args.max_verification_rounds, None,
                auditor_llm=auditor_llm,
                workspaces_dir=None if args.no_isolation else args.workspaces_dir,
                merge_lock_path=args.merge_lock,
                progress_json_path=args.progress_json,
                backend_name=args.backend_name,
            )
        except LLMError as e:
            attempt = RuleAttempt(spec.name, spec.backend, spec.description, "FAILED", 0, f"LLM error: {e}")
        except Exception as e:
            attempt = RuleAttempt(spec.name, spec.backend, spec.description, "FAILED", 0, f"agent error: {e}")
        elapsed = time.time() - start
        pool_result = pool.mark_outcome(name, attempt.status)
        print(f"[{worker_id}] -> {attempt.status} ({elapsed:.1f}s) [pool: {pool_result}]")
        progress.record(attempt)


def main():
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    src = parser.add_mutually_exclusive_group(required=True)
    src.add_argument("--spec", type=Path, help="Single rule-spec file to port.")
    src.add_argument("--spec-dir", type=Path, help="Directory of rule-spec files to port, one rule each.")
    parser.add_argument(
        "--pool", action="store_true",
        help="Run --spec-dir as a shared work pool instead of a fixed sequential batch: "
             "--workers parallel processes each claim one rule at a time (file-locked, so "
             "no two workers ever take the same one), run it, and go back for the next. A "
             "rule that comes back FAILED is put back in the pool once for a second attempt "
             "(possibly by a different worker) before being recorded as terminally FAILED. "
             "Requires --spec-dir.",
    )
    parser.add_argument(
        "--workers", type=int, default=4,
        help="Parallel worker processes for --pool (ignored otherwise).",
    )
    parser.add_argument("--pool-worker", type=Path, help=argparse.SUPPRESS)
    parser.add_argument("--name", help="Override the rule name (single-spec mode only).")
    parser.add_argument("--backend", help="Override the source backend label (single-spec mode only).")
    parser.add_argument("--max-turns", type=int, default=20,
                         help="Porter tool-calling turns (exploration + try_rule calls) per verification round.")
    parser.add_argument("--max-verification-rounds", type=int, default=5,
                         help="How many times the verifier may send the porter back to try again "
                              "(a round ends early as soon as the porter writes a version of the "
                              "rule that gets a QED verdict, so this is a cap, not a target).")
    parser.add_argument("--provider", choices=["anthropic", "openai"], default=None)
    parser.add_argument("--model", default=None)
    parser.add_argument("--api-key", default=None)
    parser.add_argument("--endpoint", default=None)
    parser.add_argument("--max-tokens", type=int, default=24576,
                         help="Per-request completion token budget. Reasoning models need "
                              "headroom for their thinking tokens before the real answer; kept "
                              "well below a typical model's total context window (not unlimited) "
                              "so there's still room left for the prompt. Auto-shrinks further, "
                              "per request, only if a specific model's actual context limit is hit.")
    parser.add_argument("--timeout", type=float, default=600.0,
                         help="Per-request HTTP timeout in seconds. Reasoning models can take "
                              "a long time on harder rules.")
    parser.add_argument("--verifier-provider", choices=["anthropic", "openai"], default=None,
                         help="Defaults to --provider if unset.")
    parser.add_argument("--verifier-model", default=None, help="Defaults to --model if unset.")
    parser.add_argument("--verifier-api-key", default=None, help="Defaults to --api-key if unset.")
    parser.add_argument("--verifier-endpoint", default=None, help="Defaults to --endpoint if unset.")
    parser.add_argument("--verifier-max-tokens", type=int, default=None, help="Defaults to --max-tokens if unset.")
    parser.add_argument("--verifier-timeout", type=float, default=None, help="Defaults to --timeout if unset.")
    parser.add_argument("--no-verifier", action="store_true",
                         help="Skip independent verification (not recommended; PROGRESS.md will say so).")
    parser.add_argument("--auditor-provider", choices=["anthropic", "openai"], default=None,
                         help="Defaults to --verifier-provider (then --provider) if unset.")
    parser.add_argument("--auditor-model", default=None, help="Defaults to --verifier-model if unset.")
    parser.add_argument("--auditor-api-key", default=None, help="Defaults to --verifier-api-key if unset.")
    parser.add_argument("--auditor-endpoint", default=None, help="Defaults to --verifier-endpoint if unset.")
    parser.add_argument("--auditor-max-tokens", type=int, default=None, help="Defaults to --verifier-max-tokens if unset.")
    parser.add_argument("--auditor-timeout", type=float, default=None, help="Defaults to --verifier-timeout if unset.")
    parser.add_argument("--no-auditor", action="store_true",
                         help="Skip the independent LLM review of DSL extensions (the mechanical "
                              "fresh re-proof of every previously-proved rule still always runs "
                              "before any extension is kept).")
    parser.add_argument(
        "--repo", type=Path, default=ROOT_DIR / "vendor" / "rulescript-repo",
        help="Path to the RuleScript maven project.",
    )
    parser.add_argument(
        "--backend-root", type=Path, default=ROOT_DIR / "vendor" / "calcite-src",
        help="Path to the vendored source checkout (Calcite, CockroachDB, ...) the porter reads from.",
    )
    parser.add_argument(
        "--backend-name", type=str, default="calcite",
        help="Tool-schema root name the porter uses for --backend-root, e.g. 'calcite' or "
             "'cockroach' — must match the backend field in your rule specs' naming convention.",
    )
    parser.add_argument(
        "--qed-prover",
        type=Path,
        default=ROOT_DIR / "vendor" / "qed-prover" / "target" / "release" / "qed-prover",
        help="Path to the built qed-prover binary.",
    )
    parser.add_argument("--progress-md", type=Path, default=ROOT_DIR / "calcite" / "PROGRESS.md")
    parser.add_argument("--progress-json", type=Path, default=ROOT_DIR / "calcite" / "progress.json")
    parser.add_argument(
        "--rules-out-dir", type=Path, default=ROOT_DIR / "calcite" / "rules",
        help="Where the human-readable mirror of each rule's outcome is published. "
             "Point this (and --progress-md/--progress-json/--spec-dir) at a different "
             "backend's own folder (e.g. cockroach/) when porting for that backend.",
    )
    parser.add_argument(
        "--no-isolation", action="store_true",
        help="Work directly against --repo instead of giving each rule its own private copy. "
             "Only safe for a single rule at a time with nothing else touching --repo "
             "concurrently — with --isolation (the default), several port_rule.py processes "
             "can safely run at once, each porting a different rule, merging into --repo only "
             "once a rule is actually QED-confirmed.",
    )
    parser.add_argument(
        "--workspaces-dir", type=Path, default=ROOT_DIR / ".cache" / "workspaces",
        help="Where each rule's isolated private copy of --repo is created (ignored with --no-isolation).",
    )
    parser.add_argument(
        "--merge-lock", type=Path, default=ROOT_DIR / ".cache" / "merge.lock",
        help="Cross-process lock file guarding the merge-into-main step, so two concurrently "
             "running port_rule.py processes never merge at the same time (ignored with --no-isolation).",
    )
    parser.add_argument(
        "--offline-code",
        type=Path,
        help="Skip the LLM for the porter's first turn and directly call try_rule with this "
        "Java file's contents (still goes through compile/JSON/prove for real, and, unless "
        "--no-verifier, still gets independently reviewed). For testing the harness without an API key.",
    )
    args = parser.parse_args()
    args.rules_out_dir = args.rules_out_dir.resolve()

    if not args.qed_prover.exists():
        parser.error(f"qed-prover binary not found at {args.qed_prover}; build it first.")
    if not (args.repo / "mvnw").exists():
        parser.error(f"{args.repo} doesn't look like the RuleScript maven project (no mvnw found).")

    specs: list[RuleSpec] = []
    if args.spec:
        specs.append(parse_spec_file(args.spec, args.name, args.backend))
    else:
        files = sorted(p for p in args.spec_dir.iterdir() if p.suffix in (".md", ".txt") and p.is_file())
        if not files:
            parser.error(f"No .md/.txt spec files found in {args.spec_dir}")
        specs = [parse_spec_file(p) for p in files]

    offline_code_text = args.offline_code.read_text() if args.offline_code else None

    llm = None
    verifier_llm = None
    auditor_llm = None
    if offline_code_text is None:
        llm = build_llm(args.provider, args.model, args.api_key, args.endpoint, args.max_tokens, args.timeout)
    if not args.no_verifier:
        verifier_llm = build_llm(
            args.verifier_provider or args.provider,
            args.verifier_model or args.model,
            args.verifier_api_key or args.api_key,
            args.verifier_endpoint or args.endpoint,
            args.verifier_max_tokens or args.max_tokens,
            args.verifier_timeout or args.timeout,
        )
    if not args.no_auditor:
        auditor_llm = build_llm(
            args.auditor_provider or args.verifier_provider or args.provider,
            args.auditor_model or args.verifier_model or args.model,
            args.auditor_api_key or args.verifier_api_key or args.api_key,
            args.auditor_endpoint or args.verifier_endpoint or args.endpoint,
            args.auditor_max_tokens or args.verifier_max_tokens or args.max_tokens,
            args.auditor_timeout or args.verifier_timeout or args.timeout,
        )

    pipeline = Pipeline(args.repo, args.qed_prover, rules_out_dir=args.rules_out_dir)
    progress = ProgressLog(args.progress_json, args.progress_md)

    if args.pool_worker is not None:
        specs_by_name = {s.name: s for s in specs}
        run_pool_worker(
            Pool(args.pool_worker), f"worker-{os.getpid()}", specs_by_name,
            pipeline, llm, verifier_llm, auditor_llm, args, progress,
        )
        return

    if args.pool:
        if not args.spec_dir:
            parser.error("--pool requires --spec-dir")
        state_path = (ROOT_DIR / ".cache" / "pool" / f"{args.spec_dir.name}.json").resolve()
        state_path.parent.mkdir(parents=True, exist_ok=True)
        pool = Pool(state_path)
        pool.init([s.name for s in specs])
        print(f"Pool state at {state_path} — {len(specs)} rule(s), {args.workers} worker(s)")
        child_argv = list(sys.argv[1:]) + ["--pool-worker", str(state_path)]
        procs = [
            subprocess.Popen([sys.executable, str(Path(__file__).resolve())] + child_argv)
            for _ in range(args.workers)
        ]
        for p in procs:
            p.wait()
        summary = pool.summary()
        counts: dict[str, int] = {}
        print("\n=== Pool Summary ===")
        for name, entry in sorted(summary.items()):
            counts[entry["status"]] = counts.get(entry["status"], 0) + 1
            print(f"  {entry['status']:12s} {name} (attempts={entry['attempts']})")
        print(f"\n{counts}")
        print(f"\nProgress written to {args.progress_md} and {args.progress_json}")
        return

    results = []
    for spec in specs:
        start = time.time()
        try:
            attempt = run_one(
                spec, pipeline, llm, verifier_llm, args.repo, args.backend_root,
                args.max_turns, args.max_verification_rounds, offline_code_text,
                auditor_llm=auditor_llm,
                workspaces_dir=None if args.no_isolation else args.workspaces_dir,
                merge_lock_path=args.merge_lock,
                progress_json_path=args.progress_json,
                backend_name=args.backend_name,
            )
        except LLMError as e:
            attempt = RuleAttempt(spec.name, spec.backend, spec.description, "FAILED", 0, f"LLM error: {e}")
        except Exception as e:
            attempt = RuleAttempt(spec.name, spec.backend, spec.description, "FAILED", 0, f"agent error: {e}")
        elapsed = time.time() - start
        print(f"   -> {attempt.status} ({elapsed:.1f}s, {attempt.verification_rounds_used} round(s))")
        progress.record(attempt)
        results.append(attempt)

    print("\n=== Summary ===")
    for r in results:
        print(f"  {r.status:8s} {r.rule_name}")
    print(f"\nProgress written to {args.progress_md} and {args.progress_json}")


if __name__ == "__main__":
    main()
