# SimplifyZeroCardinalityAntiJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 88  **Verification rounds used:** 5
**Scope detail:** the ^(CanHaveZeroRows $right) precondition is encoded as a concrete one-row VALUES relation (single INTEGER column) rather than an arbitrary non-empty relation, and the correlated AntiJoinApply variant is not modeled.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

SimplifyZeroCardinalityAntiJoin converts an AntiJoin operator to an empty
Values when it's known that the right input never returns zero rows, and
there is no join condition.

Extracted from `join.opt` (which defines multiple rules — implement specifically `SimplifyZeroCardinalityAntiJoin`, not the other rules in that file):

```
# SimplifyZeroCardinalityAntiJoin converts an AntiJoin operator to an empty
# Values when it's known that the right input never returns zero rows, and
# there is no join condition.
[SimplifyZeroCardinalityAntiJoin, Normalize]
(AntiJoin | AntiJoinApply
    $left:*
    $right:* & ^(CanHaveZeroRows $right)
    []
)
=>
(ConstructEmptyValues (OutputCols $left))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding matches the rule's exact shape — an ANTI join of an uninterpreted left relation with the source's empty on-list rendered as a TRUE condition, rewritten to the empty relation over the left's output type — and the proof is non-vacuous: had the join kind been inner, the condition false, or the right side empty, the equivalence with the empty relation would have failed for non-empty left bags. The declared PARTIAL scope is honest and genuinely forced rather than a shortcut: QED's table format can only carry key constraints and row-level "guaranteed" predicates, both vacuous over the empty bag, so the `^(CanHaveZeroRows $right)` precondition (right is guaranteed non-empty) cannot be stated about an uninterpreted symbol and must be witnessed by a concrete non-empty relation (a one-row VALUES), and the correlated AntiJoinApply variant is not modelable because the DSL's `Correlate` builder always derives its condition from all left/right fields, so no empty/TRUE-apply condition can be expressed.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 118834
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
    "nanos": 1080375
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 334750
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 9621584
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 26388541
  }
}
```
