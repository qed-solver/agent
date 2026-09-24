# Name: EliminateGroupBy
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateGroupBy is similar to EliminateDistinct, but it operates on GroupBy
expressions where the grouping columns are statically known to form a strict
key in the GroupBy's input.

It only applies if all the aggregate functions are ConstAgg, ConstNotNullAgg,
or AnyNotNullAgg. These aggregate functions all evaluate to the first non-NULL
value encountered, and NULL if there are no such values. Because the input is
guaranteed to produce one row per group, these aggregate functions are
equivalent to projecting their input column.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateGroupBy`, not the other rules in that file):

```
# EliminateGroupBy is similar to EliminateDistinct, but it operates on GroupBy
# expressions where the grouping columns are statically known to form a strict
# key in the GroupBy's input.
#
# It only applies if all the aggregate functions are ConstAgg, ConstNotNullAgg,
# or AnyNotNullAgg. These aggregate functions all evaluate to the first non-NULL
# value encountered, and NULL if there are no such values. Because the input is
# guaranteed to produce one row per group, these aggregate functions are
# equivalent to projecting their input column.
[EliminateGroupBy, Normalize]
(GroupBy
    $input:*
    $aggs:* & (AreAllAnyNotNullAggs $aggs)
    $groupingPrivate:* &
        (ColsAreStrictKey (GroupingCols $groupingPrivate) $input)
)
=>
(Project
    $input
    (ConvertAnyNotNullAggsToProjections $aggs)
    (IntersectionCols
        (GroupingOutputCols $groupingPrivate $aggs)
        (OutputCols $input)
    )
)
```
