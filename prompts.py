"""Prompt construction for the porter agent's tool-using loop."""
from __future__ import annotations

from pathlib import Path

REFERENCE_PATH = Path(__file__).resolve().parent / "rulescript_reference.md"


def system_prompt() -> str:
    return REFERENCE_PATH.read_text()


def initial_user_prompt(rule_name: str, source_backend: str, source_path: str, source_hint: str) -> str:
    return f"""Port a query-rewrite rule from **{source_backend}** into RuleScript.

Target Java record name / file name: `{rule_name}`
Source file to read (root `"calcite"`): `{source_path}`

{source_hint}

You have these tools:
- `list_directory(root, path)` / `search_code(root, query, path=None)` /
  `find_symbol(root, symbol)` / `read_file(root, path, start_line=None, end_line=None)`
  — explore either root: `"calcite"` (the source backend, rooted at
  `core/src/main/java/org/apache/calcite/`) or `"rulescript"` (the DSL,
  rooted at `src/main/java/org/qed/` — read `RelRN.java`/`RexRN.java`/
  `RRule.java`/`RelType.java` there if you need the exact API beyond what's
  in the reference doc; don't guess at a method signature).
- `try_rule(java_source)` — compile your candidate, serialize it, and run
  the real QED prover on it. Returns exactly what failed (compile error /
  JSON-generation error / QED's provable result with stats) so you can
  iterate. Call it as many times as you need.
- `extend_dsl_file(file, old_snippet, new_snippet, reason)` — if the rule
  genuinely needs an operator/shape `RelRN`/`RexRN`/`JSONSerializer` doesn't
  expose yet, use this to add it properly rather than settling for a
  narrower rule. This is a find-and-replace edit, not a full-file rewrite —
  `old_snippet` must match the file's exact current text (read it fresh
  first) and occur exactly once; keep the edit as small as the actual
  change needs. Safe to attempt: automatically reverted if it fails to
  compile or breaks any already-proved rule.

Start by reading the source rule file. Then, if needed, look at the DSL
files. Then iterate with `try_rule` until it reports `provable: true`, or
reply with exactly `UNSUPPORTED: <reason>` if you've concluded (per the
Limitations section) that it can't be encoded. If what's blocking you is a
missing DSL capability rather than a fundamental QED limitation, try
`extend_dsl_file` before falling back to a narrower special case (see
"Declaring scope honestly" / "Hard boundary").
"""


def nudge_use_tool_or_conclude() -> str:
    return (
        "You replied with plain text without calling a tool. Either call a "
        "tool to keep exploring or test a candidate with `try_rule`, or — "
        "only if you've concluded the rule genuinely can't be encoded — "
        "reply with exactly `UNSUPPORTED: <reason>` and nothing else."
    )


def truncate(text: str, limit: int = 1500) -> str:
    text = text.strip()
    if len(text) <= limit:
        return text
    return text[:limit] + f"\n... [truncated, {len(text) - limit} more characters]"
