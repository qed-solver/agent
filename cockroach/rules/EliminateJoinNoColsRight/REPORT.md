# EliminateJoinNoColsRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 49  **Verification rounds used:** 3
**Scope detail:** covers the non-correlated InnerJoin operator; the correlated InnerJoinApply variant of the source rule is not modeled.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

EliminateJoinNoColsRight eliminates an InnerJoin with a one row, zero column
right input set. These can be produced when a Values, scalar GroupBy, or other
one-row operator's columns are never used.

Extracted from `join.opt` (which defines multiple rules — implement specifically `EliminateJoinNoColsRight`, not the other rules in that file):

```
# EliminateJoinNoColsRight eliminates an InnerJoin with a one row, zero column
# right input set. These can be produced when a Values, scalar GroupBy, or other
# one-row operator's columns are never used.
[EliminateJoinNoColsRight, Normalize]
(InnerJoin | InnerJoinApply
    $left:*
    $right:* &
        (ColsAreEmpty (OutputCols $right)) &
        (HasOneRow $right)
    $on:*
)
=>
(Select $left $on)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

before() (INNER join whose right input is a one-row, zero-column Values) and after() (Filter over the left) are genuinely distinct shapes, and the proven equivalence — join with the bag-theoretic unit relation equals a left-only filter — is exactly the source rule's semantics: since the right has zero columns, $on is necessarily left-only, and the single right row preserves each matching left row's multiplicity, so the unit-Values encoding faithfully captures the HasOneRow/ColsAreEmpty preconditions for *any* one-row zero-column operator rather than under-generalizing (the single-column L and name-shared "on" correctly stand in for arbitrary inputs/conditions, and the successful proof confirms the "on" symbol unified across before/after, ruling out a distinct-symbol accident). The only true narrowing — the correlated InnerJoinApply variant, whose outer-dependent right cannot be structurally expressed in the DSL — is honest and specifically flagged in the SCOPE: PARTIAL line, and the non-apply case is a genuine, non-degenerate fragment of the rule.

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
    "nanos": 840708
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 298083
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
    "nanos": 1496834
  }
}
```
