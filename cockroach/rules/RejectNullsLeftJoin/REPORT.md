# RejectNullsLeftJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 27  **Verification rounds used:** 3
**Scope detail:** the null-rejecting filter is restricted to an explicit IS_NOT_NULL on the right input's column, and only the LeftJoin→InnerJoin variant is modeled (FullJoin→RightJoin and the Apply variants are not)


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/reject_nulls.opt

RejectNullsLeftJoin reduces a LeftJoin operator to an InnerJoin operator (or a
FullJoin to a RightJoin) when there is a null-rejecting filter on any column
from the right side. The effect of the null-rejecting filter is that output
rows with all NULL values on the right side created by the left (or full) join
are eliminated, making the join equivalent to an inner (or right) join. For
example:

SELECT * FROM a LEFT OUTER JOIN b ON a.x = b.x WHERE b.y < 5

can be reduced to:

SELECT * FROM a INNER JOIN b ON a.x = b.x WHERE b.y < 5

since b.y < 5 is a null-rejecting filter on the right side.

This rule is marked as high priority so that it runs before Select filter
pushdown rules. Those rules may remove a filter before it's had a chance to
rewrite the input join.

Citations: [1]

Extracted from `reject_nulls.opt` (which defines multiple rules — implement specifically `RejectNullsLeftJoin`, not the other rules in that file):

```
# RejectNullsLeftJoin reduces a LeftJoin operator to an InnerJoin operator (or a
# FullJoin to a RightJoin) when there is a null-rejecting filter on any column
# from the right side. The effect of the null-rejecting filter is that output
# rows with all NULL values on the right side created by the left (or full) join
# are eliminated, making the join equivalent to an inner (or right) join. For
# example:
#
#   SELECT * FROM a LEFT OUTER JOIN b ON a.x = b.x WHERE b.y < 5
#
# can be reduced to:
#
#   SELECT * FROM a INNER JOIN b ON a.x = b.x WHERE b.y < 5
#
# since b.y < 5 is a null-rejecting filter on the right side.
#
# This rule is marked as high priority so that it runs before Select filter
# pushdown rules. Those rules may remove a filter before it's had a chance to
# rewrite the input join.
#
# Citations: [1]
[RejectNullsLeftJoin, Normalize, HighPriority]
(Select
    $input:(LeftJoin | LeftJoinApply | FullJoin
        $left:*
        $right:*
        $on:*
        $private:*
    )
    $filters:* &
        (HasNullRejectingFilter $filters (OutputCols $right))
)
=>
(Select
    (ConstructNonLeftJoin
        (OpName $input)
        $left
        $right
        $on
        $private
    )
    $filters
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's core semantic transformation (a null-rejecting filter on the right side of a LEFT JOIN makes the null-extended rows disappear, reducing it to an INNER JOIN) with the filter position and join-condition sharing matching the source rule exactly. The restriction to the concrete IS_NOT_NULL operator is a genuine QED limitation (an uninterpreted predicate could return TRUE on NULL, breaking the equivalence, so QED must interpret the operator to verify null-rejection), and the omission of FullJoin→RightJoin and Apply variants is a real, honestly-documented narrowing. The proof is non-vacuous (LEFT vs INNER is a semantic difference that only holds because IS_NOT_NULL filters the null-extended rows), symbols are correctly independent/shared, and no preconditions are missing. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7172418
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
    "nanos": 868250
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 672625
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19579667
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 35165292
  }
}
```
