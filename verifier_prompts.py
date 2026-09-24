from __future__ import annotations

from pathlib import Path

from dsl_files import load_dsl_files

REFERENCE_PATH = Path(__file__).resolve().parent / "rulescript_reference.md"

VERIFIER_ROLE_PREAMBLE = """You are the independent verifier in a two-agent pipeline. A separate
"porter" agent already attempted to express a query-optimizer rewrite rule
in RuleScript (reference below) and ran it through compilation and the QED
prover. You did not write that code and have no memory of doing so — review
it fresh and skeptically. Your job is quality control, not encouragement.

You will be asked to review one of two situations. Follow the exact output
format requested for that situation; it will be machine-parsed.
"""


def system_prompt(repo_dir: Path) -> str:
    return VERIFIER_ROLE_PREAMBLE + "\n\n" + REFERENCE_PATH.read_text() + "\n\n" + load_dsl_files(repo_dir)


def review_proved(rule_name: str, backend: str, source_text: str, java_code: str, prover_json: dict) -> str:
    return f"""## Situation: QED reported this encoding PROVABLE — verify it is faithful

Rule name: `{rule_name}`
Source backend: {backend}

Full original source rule (the porter only had a `read_file` tool and may
not have read every line of this — you are seeing all of it):
-----
{source_text}
-----

The porter's final RuleScript encoding (this is what QED proved
`before()` == `after()` for, for all instantiations of its uninterpreted
symbols):
-----
{java_code}
-----

qed-prover's raw result: {prover_json}

Check specifically for these failure modes, which would make a "provable"
result meaningless or misleading:

1. **Triviality**: does `before()` actually differ from `after()` in a way
   that reflects the real optimization (e.g. did the porter accidentally
   encode `before() == after()` structurally, so the proof is vacuous)?
2. **Under-generalization**: did the porter hard-code something that should
   have been an uninterpreted symbol (e.g. baking in a concrete predicate
   like `x > 5` instead of an uninterpreted `pred(...)`, or fixing a join
   type that the source rule actually applies to several join types), so
   the proof covers a much narrower family of queries than the real rule?
3. **Wrong operators**: does the encoding use the right relational shape
   (right join kind, right union/intersect/minus `all` flag, right operator
   nesting) to match what the source rule actually rewrites, rather than a
   superficially-similar but semantically different pattern?
4. **Symbol sharing errors that happen to still prove**: e.g. reusing one
   symbol where the source rule actually needs two independent ones (this
   can make an unsound-in-reality rewrite falsely appear provable only
   because the encoding is coincidentally over-constrained).
5. **Missing preconditions**: did the source rule require a constraint
   (e.g. a primary key, a NOT NULL guarantee handled via the source
   language's own null semantics) that is silently absent from the
   encoding, such that the "proof" is of a different, easier claim?
6. **Scope honesty**: is this the full rule, or a narrower special case? If
   the code doesn't say, decide for yourself by comparing against the full
   source above. If it's narrower, is the restriction genuine and specific
   (not vague), and is the result still a useful, non-degenerate rule
   (not narrowed to the point where `before()` and `after()` are forced
   structurally identical)? Note the answer in your reasoning either way.

Reply with **exactly** this format (nothing else):

```
VERDICT: CONFIRMED
REASONING: <1-3 sentences on why this is a faithful, general encoding of the source rule>
```

or, if you find a real problem:

```
VERDICT: REJECTED
REASONING: <specifically what is wrong and, if you can tell, what a correct encoding should do differently>
```
"""


def review_unsupported(
    rule_name: str, backend: str, source_description: str, claimed_reason: str, transcript_tail: str
) -> str:
    return f"""## Situation: the porter concluded this rule is UNSUPPORTED by RuleScript/QED — verify that claim

Rule name: `{rule_name}`
Source backend: {backend}

Original source rule as given to the porter:
-----
{source_description}
-----

The porter's stated reason for giving up:
-----
{claimed_reason}
-----

Tail of the porter's attempt transcript (its last attempt(s), any compile
errors or QED "not provable" results it saw), for context:
-----
{transcript_tail}
-----

Your job: independently decide whether this rule is **genuinely** outside
what RuleScript's core language + QED can prove (per the Limitations
section of the reference above — e.g. it fundamentally needs row order,
`Sort`/`Limit`/`Offset`/`Window`/`Sample`, bag-variant intersect/minus, an
aggregate algebraic identity QED can't know, or an operator whose specific
internal semantics QED cannot see through as an uninterpreted function) —
or whether the porter simply didn't find the right encoding and a real
attempt would likely succeed (e.g. it gave up after a symbol-sharing bug it
never diagnosed, or the rule is well within the core language but the
porter mismodeled it).

Reply with **exactly** this format (nothing else):

```
VERDICT: AGREE
REASONING: <1-3 sentences citing the specific fundamental limitation that applies, in your own words, for the progress log>
```

or, if you think another attempt should succeed:

```
VERDICT: DISAGREE
REASONING: <what you think the porter got wrong, and a concrete suggestion for how to encode it instead>
```
"""


def parse_verdict(reply: str) -> tuple[str | None, str]:
    verdict = None
    reasoning_lines = []
    in_reasoning = False
    for line in reply.splitlines():
        stripped = line.strip()
        if stripped.upper().startswith("VERDICT:"):
            verdict = stripped.split(":", 1)[1].strip().upper()
            in_reasoning = False
        elif stripped.upper().startswith("REASONING:"):
            reasoning_lines.append(stripped.split(":", 1)[1].strip())
            in_reasoning = True
        elif in_reasoning:
            reasoning_lines.append(stripped)
    return verdict, " ".join(l for l in reasoning_lines if l).strip()
