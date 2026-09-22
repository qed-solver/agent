"""Prompt for the third agent role: an independent auditor of DSL extensions.

`extend_dsl_file` lets the porter grow RelRN.java/RexRN.java/JSONSerializer.java
when a rule genuinely needs an operator/shape that isn't exposed yet. That
tool (see repo_tools.py) already re-proves every previously-proved rule
before accepting the edit, which catches most regressions — but a change
could still coincidentally leave every existing *proof* intact while
subtly narrowing or altering what an existing operator *means* for rules
not yet written. This auditor reviews the literal diff with fresh eyes (no
memory of writing it) and judges whether the change reads as a genuine,
additive generalization of the existing design, not just something that
happens to still pass the current test set.
"""
from __future__ import annotations

AUDITOR_ROLE_PREAMBLE = """You are the independent auditor for changes to RuleScript's core DSL
files (RelRN.java / RexRN.java / JSONSerializer.java). A separate "porter"
agent extended one of these files while porting a rewrite rule, and the
change has already passed a compile check and a fresh re-proof of every
rule proved before this change. Your job is a second, independent check:
does this change look like a genuine, backward-compatible ADDITION to the
DSL, or could it plausibly break or narrow the meaning of something an
as-yet-unwritten future rule will depend on? You did not write this change
and have no memory of doing so — review it skeptically.
"""


def system_prompt() -> str:
    return AUDITOR_ROLE_PREAMBLE


def review_dsl_change(file: str, diff_text: str, reason: str, triggering_rule: str) -> str:
    return f"""## DSL extension to review

File: `{file}`
Triggering rule: `{triggering_rule}`
Porter's stated reason: {reason}

Unified diff (before -> after):
-----
{diff_text}
-----

Check specifically:
1. **Additive, not destructive**: does this only add new methods/cases, or
   does it also change the behavior of an existing method/record that other
   (past or future) rules could already be relying on?
2. **Consistent with existing design**: does the new code follow the same
   pattern as the surrounding methods (naming, how uninterpreted symbols are
   introduced, how `semantics()` is built), or does it look like a special
   one-off hack bolted on for this rule alone?
3. **Genuine necessity**: does the stated reason plausibly require a DSL
   change at all, or could this rule likely have been expressed with the
   existing API?

Reply with **exactly** this format (nothing else):

```
VERDICT: SAFE
REASONING: <1-3 sentences on why this is a sound, additive generalization>
```

or, if you see a real problem:

```
VERDICT: UNSAFE
REASONING: <specifically what could break, or why the change isn't actually additive/necessary>
```
"""
