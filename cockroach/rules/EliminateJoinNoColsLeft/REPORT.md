# EliminateJoinNoColsLeft

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 83  **Verification rounds used:** 5
**Scope detail:** covers the non-correlated InnerJoin operator; the correlated InnerJoinApply variant of the source rule is not modeled.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

EliminateJoinNoColsLeft eliminates an InnerJoin with a one row, zero column
left input set. These can be produced when a Values, scalar GroupBy, or other
one-row operator's columns are never used.

Extracted from `join.opt` (which defines multiple rules — implement specifically `EliminateJoinNoColsLeft`, not the other rules in that file):

```
# EliminateJoinNoColsLeft eliminates an InnerJoin with a one row, zero column
# left input set. These can be produced when a Values, scalar GroupBy, or other
# one-row operator's columns are never used.
[EliminateJoinNoColsLeft, Normalize]
(InnerJoin | InnerJoinApply
    $left:* &
        (ColsAreEmpty (OutputCols $left)) &
        (HasOneRow $left)
    $right:*
    $on:*
)
=>
(Select $right $on)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` (an INNER join with a one-row, zero-column left) and `after()` (a plain filter on the right) are genuinely different shapes and the equivalence is a real join→filter reduction, not a structural no-op; the uninterpreted predicate `on` is correctly shared between the join condition (which, with a zero-column left, ranges only over the right's columns via the join fields) and the filter (over the right's own fields), and the left is a concrete representative of the rule's `HasOneRow ∧ ColsAreEmpty` precondition — which is the *unique* one-row, zero-column relation in bag semantics, so no generality is lost by hard-coding it (the provable result also confirms it is exactly one row, since a 0- or 2-row left would break the equality). The right input and join condition stay fully uninterpreted and the join kind is correctly INNER, and the only exclusion — the correlated `InnerJoinApply` variant, which the DSL has no apply-join kind to express — is honestly flagged as PARTIAL with a specific reason, leaving a non-degenerate, useful rule.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 0
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 0
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 837458
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 310167
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 0
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 1475541
  }
}
```
