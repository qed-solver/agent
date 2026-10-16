# TryRemapJoinOuterColsRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 0  **Verification rounds used:** 0
**Scope detail:** INNER apply join only, one single-column L (x) and a two-column R (a, b), a single equality outer-column reference a = x in the right Select, and a single ON equality b = x


## Source rule (as given to the porter)

```
# Name: TryRemapJoinOuterColsRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryRemapJoinOuterColsRight attempts to replace outer column references in the
right input of a join with equivalent non-outer columns using the ON filters.
It is valid to do this whenever it is possible to push the equality filter(s)
down the tree until it holds true for the outer-column reference. Using the
following query as an example:

SELECT * FROM xy INNER JOIN LATERAL (SELECT * FROM ab WHERE a = x) ON b = x

It is possible to push the 'b = x' filter down into the correlated right input
of the join, which would allow replacing the 'a = x' filter with 'a = b', thus
decorrelating the join. The match condition is similar to that for
PushFilterIntoJoinRight because TryRemapJoinOuterColsRight simulates filter
push-down when it makes the replacement.

It is desirable to attempt to fire TryRemapJoinOuterColsRight before other
decorrelation rules because it does not perform any transformations beyond the
variable replacement. This prevents situations where decorrelation rules make
the plan worse in their attempts to decorrelate the query. For example,
decorrelation can pull filters up the operator tree or hoist subqueries into
joins. This can cause plan changes which are difficult for the optimizer to
reverse, and it won't even attempt to do so if the query isn't successfully
decorrelated.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryRemapJoinOuterColsRight`, not the other rules in that file):

```
# TryRemapJoinOuterColsRight attempts to replace outer column references in the
# right input of a join with equivalent non-outer columns using the ON filters.
# It is valid to do this whenever it is possible to push the equality filter(s)
# down the tree until it holds true for the outer-column reference. Using the
# following query as an example:
#
#   SELECT * FROM xy INNER JOIN LATERAL (SELECT * FROM ab WHERE a = x) ON b = x
#
# It is possible to push the 'b = x' filter down into the correlated right input
# of the join, which would allow replacing the 'a = x' filter with 'a = b', thus
# decorrelating the join. The match condition is similar to that for
# PushFilterIntoJoinRight because TryRemapJoinOuterColsRight simulates filter
# push-down when it makes the replacement.
#
# It is desirable to attempt to fire TryRemapJoinOuterColsRight before other
# decorrelation rules because it does not perform any transformations beyond the
# variable replacement. This prevents situations where decorrelation rules make
# the plan worse in their attempts to decorrelate the query. For example,
# decorrelation can pull filters up the operator tree or hoist subqueries into
# joins. This can cause plan changes which are difficult for the optimizer to
# reverse, and it won't even attempt to do so if the query isn't successfully
# decorrelated.
[TryRemapJoinOuterColsRight, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply | SemiJoin
        | SemiJoinApply | AntiJoin | AntiJoinApply
    $left:*
    $right:* & (HasOuterCols $right)
    $on:* &
        (CanMaybeRemapOuterCols $right $on) &
        (Let ($remapped $ok):(TryRemapOuterCols $right $on) $ok)
    $private:*
)
=>
((OpName) $left $remapped $on $private)
```
```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6161292
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 7470625
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 60041
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 496208
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 12316583
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 7524000
  },
  "total_duration": {
    "secs": 0,
    "nanos": 22905250
  }
}
```
