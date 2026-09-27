# InlineConstVar

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 46  **Verification rounds used:** 3
**Scope detail:** "a variable restricted to a constant c" is modeled as an INNER equality join a = c against a unique single-column relation (the DSL has no typed constant literals), and one occurrence of the variable in one uninterpreted filter conjunct is inlined to c (a single self-retriggering iteration of the rule).


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/inline.opt

InlineConstVar inlines variables which are restricted to be constant, as in
SELECT * FROM foo WHERE a = 4 AND a IN (1, 2, 3, 4).
=>
SELECT * FROM foo WHERE a = 4 AND 4 IN (1, 2, 3, 4).
Note that a single iteration of this rule might not be sufficient to inline
all variables, in which case it will trigger itself again.

This rule is high priority so that it runs before filter pushdown.

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineConstVar`, not the other rules in that file):

```
# InlineConstVar inlines variables which are restricted to be constant, as in
#   SELECT * FROM foo WHERE a = 4 AND a IN (1, 2, 3, 4).
# =>
#   SELECT * FROM foo WHERE a = 4 AND 4 IN (1, 2, 3, 4).
# Note that a single iteration of this rule might not be sufficient to inline
# all variables, in which case it will trigger itself again.
#
# This rule is high priority so that it runs before filter pushdown.
[InlineConstVar, Normalize, HighPriority]
(Select $input:* $filters:* & (CanInlineConstVar $filters))
=>
(Select $input (InlineConstVar $filters))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The proof is non-vacuous and not coincidental: `before()` and `after()` are structurally distinct (same uninterpreted predicate `f` applied to join field 0 vs field 1), and the equivalence genuinely depends on the concrete `EQUALS` join condition `a = c` plus congruence — with an uninterpreted equality symbol it would not prove, so the porter made exactly the right symbol choices. Modeling "a variable restricted to a constant c" as an INNER join against a unique single-column relation is semantically equivalent to the real rule's conjunct-based premise (`Join_{a=c}` ≡ cross-join filtered by `a = c`), so the relational shape faithfully reproduces the source rule's example modulo tagging rows with the constant — a reasonable workaround given the DSL exposes no typed constant literals (only boolean literals in `RexRN`). The narrowing to one variable/one conjunct/one occurrence is a genuine, specific, and honestly disclosed special case that captures precisely the single-substitution step the original rule applies (and re-triggers) on, so the `PARTIAL` scope tag is accurate and the result is a useful, non-degenerate lemma rather than a vacuous identity.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5868542
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34861917
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 796334
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 382417
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16361208
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34961125
  },
  "total_duration": {
    "secs": 0,
    "nanos": 80385791
  }
}
```
