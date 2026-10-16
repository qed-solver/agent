# TryDecorrelateGroupBy

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 68  **Verification rounds used:** 5
**Scope detail:** left is assumed to already be a single-column strict-key relation (so no ordinality/ConstAgg canary is synthesized) and the right GroupBy has exactly one grouping column and one aggregate over a non-key column


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateGroupBy "pushes down" a Join into a GroupBy operator, in an
attempt to keep "digging" down to find and eliminate unnecessary correlation.
The eventual hope is to trigger the DecorrelateJoin rule to turn a JoinApply
operator into a non-apply Join operator.

Example:

SELECT left.x, left.y, input.*
FROM left
INNER JOIN LATERAL
(
SELECT COUNT(*) FROM input WHERE input.x = left.x GROUP BY c
) AS input
ON left.y = 10
=>
SELECT CONST_AGG(left.x), CONST_AGG(left.y), COUNT(*)
FROM left WITH ORDINALITY
INNER JOIN LATERAL
(
SELECT * FROM input WHERE input.x = left.x
) AS input
ON True
GROUP BY input.c, left.ordinality
HAVING left.y = 10

In other cases, we can use an existing non-null column as a canary; that
column would not be constant necessarily, hence the use of ANY_NOT_NULL
instead of CONST_AGG.

An ordinality column only needs to be synthesized if "left" does not already
have a strict key. We wrap the output in a Project operator to ensure that
the original output columns are preserved and the ordinality column is not
inadvertently added as a new output column.

CONST_AGG is an internal aggregation function used when all rows in the
grouping set have the same value on the column.

Citations: [3] (see identity #8)

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateGroupBy`, not the other rules in that file):

```
# TryDecorrelateGroupBy "pushes down" a Join into a GroupBy operator, in an
# attempt to keep "digging" down to find and eliminate unnecessary correlation.
# The eventual hope is to trigger the DecorrelateJoin rule to turn a JoinApply
# operator into a non-apply Join operator.
#
# Example:
#
#   SELECT left.x, left.y, input.*
#   FROM left
#   INNER JOIN LATERAL
#   (
#     SELECT COUNT(*) FROM input WHERE input.x = left.x GROUP BY c
#   ) AS input
#   ON left.y = 10
#   =>
#   SELECT CONST_AGG(left.x), CONST_AGG(left.y), COUNT(*)
#   FROM left WITH ORDINALITY
#   INNER JOIN LATERAL
#   (
#     SELECT * FROM input WHERE input.x = left.x
#   ) AS input
#   ON True
#   GROUP BY input.c, left.ordinality
#   HAVING left.y = 10
#
# In other cases, we can use an existing non-null column as a canary; that
# column would not be constant necessarily, hence the use of ANY_NOT_NULL
# instead of CONST_AGG.
#
# An ordinality column only needs to be synthesized if "left" does not already
# have a strict key. We wrap the output in a Project operator to ensure that
# the original output columns are preserved and the ordinality column is not
# inadvertently added as a new output column.
#
# CONST_AGG is an internal aggregation function used when all rows in the
# grouping set have the same value on the column.
#
# Citations: [3] (see identity #8)
[TryDecorrelateGroupBy, Normalize]
(InnerJoin | InnerJoinApply
    $left:*
    $right:* &
        (HasOuterCols $right) &
        (GroupBy | DistinctOn
            $input:*
            $aggregations:*
            $groupingPrivate:*
        ) &
        (IsUnorderedGrouping $groupingPrivate)
    $on:*
    $private:*
)
=>
(Project
    # Needed to project away any columns added by EnsureKey.
    (Select
        ((OpName $right)
            (InnerJoinApply
                $newLeft:(EnsureKey $left)
                $input
                []
                $private
            )
            (AppendAggCols
                $aggregations
                ConstAgg
                (NonKeyCols $newLeft)
            )
            (AddColsToGrouping
                $groupingPrivate
                (KeyCols $newLeft)
            )
        )
        $on
    )
    []
    (OutputCols2 $left $right)
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-vacuous special case of the source rule: `before()` (join L with pre-grouped R, then filter `on`) and `after()` (cross-join L⋈R, group by (l,g) with the same aggregate `f`, then filter `on`) are structurally distinct yet provably equivalent precisely because `left` is declared a strict key (`unique=true`), which guarantees each left

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 10892373
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 46959709
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 974208
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 948125
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 25411792
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 47238958
  },
  "total_duration": {
    "secs": 0,
    "nanos": 88328792
  }
}
```
