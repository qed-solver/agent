from __future__ import annotations

from pathlib import Path

REFERENCE_PATH = Path(__file__).resolve().parent / "rulescript_reference.md"


def system_prompt() -> str:
    return REFERENCE_PATH.read_text()


def initial_user_prompt(rule_name: str, source_backend: str, source_path: str, source_hint: str, backend_name: str = "calcite") -> str:
    return f"""Port a query-rewrite rule from **{source_backend}** into RuleScript.

Target Java record name / file name: `{rule_name}`
Source file to read (root `"{backend_name}"`): `{source_path}`

{source_hint}

Suggested order: read the source rule file first. Check `list_ported_rules`
for a structurally similar precedent (any backend) before assuming you need
something new. If the DSL reference above doesn't pin down an exact method
signature, read the real `RelRN.java`/`RexRN.java` rather than guessing.
Iterate with `try_rule` until `provable: true`, using `extend_dsl_file` if
the blocker is a missing DSL capability rather than a fundamental QED
limitation (see "Declaring scope honestly" / "Hard boundary"). If you
conclude the rule genuinely can't be encoded, reply with exactly
`UNSUPPORTED: <reason>`.
"""


def nudge_use_tool_or_conclude() -> str:
    return (
        "You replied with plain text without calling a tool. Either call a "
        "tool to keep exploring or test a candidate with `try_rule`, or — "
        "only if you've concluded the rule genuinely can't be encoded — "
        "reply with exactly `UNSUPPORTED: <reason>` and nothing else."
    )


def nudge_stop_deliberating() -> str:
    return (
        "Your last several replies were cut off at the token limit before producing any "
        "tool call — you're spending your whole budget on internal reasoning and never "
        "acting on it. Stop deliberating further. On this next reply, immediately call a "
        "tool with your current best guess: either `try_rule` with whatever candidate "
        "encoding you have in mind, or a narrowly-scoped lookup (`read_file`/`search_code`/"
        "`find_symbol`) for one specific missing fact. Keep any reasoning brief and act now."
    )


def truncate(text: str, limit: int = 1500) -> str:
    text = text.strip()
    if len(text) <= limit:
        return text
    return text[:limit] + f"\n... [truncated, {len(text) - limit} more characters]"
