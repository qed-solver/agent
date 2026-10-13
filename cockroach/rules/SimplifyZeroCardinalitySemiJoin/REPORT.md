# SimplifyZeroCardinalitySemiJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2
**Scope detail:** the HasZeroRows precondition is encoded as a literal .empty() relation (a statically known-empty shape) and the join filter is fixed to an uninterpreted on-condition


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

SimplifyZeroCardinalitySemiJoin converts a SemiJoin operator to an empty
Values when it's known that the right input never returns any rows.

Extracted from `join.opt` (which defines multiple rules — implement specifically `SimplifyZeroCardinalitySemiJoin`, not the other rules in that file):

```
# SimplifyZeroCardinalitySemiJoin converts a SemiJoin operator to an empty
# Values when it's known that the right input never returns any rows.
[SimplifyZeroCardinalitySemiJoin, Normalize]
(SemiJoin | SemiJoinApply
    $left:*
    $right:* & (HasZeroRows $right)
)
=>
(ConstructEmptyValues (OutputCols $left))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures a genuine special case of the source rule: it proves that SEMI(left, on, ∅) ≡ ∅(left's columns) for all instantiations of the uninterpreted left table and join predicate, which is non-trivial (before is a Join node, after is a bare LogicalValues). The narrowing from "HasZeroRows(right)" (a dynamic optimizer-known property) to "right is a literal .empty()" is the only way to express zero-cardinality in this DSL (no cardinality-constraint mechanism exists for arbitrary subexpressions), and the SCOPE: PARTIAL line correctly and specifically identifies this. The join kind (SEMI), the output column set (left's columns only, as semi-join dictates), and the empty-after construction all match the source rule's ConstructEmptyValues(OutputCols $left) semantics. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 119375
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
    "nanos": 800916
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 248917
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 9654000
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 25703959
  }
}
```
