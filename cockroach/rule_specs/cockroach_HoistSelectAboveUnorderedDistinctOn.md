# Name: HoistSelectAboveUnorderedDistinctOn
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistSelectAboveUnorderedDistinctOn hoists correlated filter conditions from
inside an unordered DistinctOn's input Select to above the DistinctOn. This
is the reverse of PushSelectIntoUnorderedDistinctOn and is valid because an
unordered DistinctOn can choose any row from each group, so filtering before
or after the grouping produces equivalent results.

This rule aids decorrelation by moving correlated filters to a position where
TryDecorrelateSelect can handle them, avoiding the more expensive
TryDecorrelateGroupBy transformation that requires EnsureKey and additional
ConstAgg columns.

Uncorrelated filters remain inside the DistinctOn for early filtering. If a
correlated filter references an input column that is not already in the
DistinctOn's output, a FirstAgg aggregation is added for that column so
it becomes available above the DistinctOn.

Example:
DistinctOn(Select(input, [s = outer.s, i > 0]), aggs, priv)
=>
Select(DistinctOn(Select(input, [i > 0]), aggs', priv), [s = outer.s])
(where aggs' = aggs + FirstAgg(s) if s was not already projected)

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistSelectAboveUnorderedDistinctOn`, not the other rules in that file):

```
# HoistSelectAboveUnorderedDistinctOn hoists correlated filter conditions from
# inside an unordered DistinctOn's input Select to above the DistinctOn. This
# is the reverse of PushSelectIntoUnorderedDistinctOn and is valid because an
# unordered DistinctOn can choose any row from each group, so filtering before
# or after the grouping produces equivalent results.
#
# This rule aids decorrelation by moving correlated filters to a position where
# TryDecorrelateSelect can handle them, avoiding the more expensive
# TryDecorrelateGroupBy transformation that requires EnsureKey and additional
# ConstAgg columns.
#
# Uncorrelated filters remain inside the DistinctOn for early filtering. If a
# correlated filter references an input column that is not already in the
# DistinctOn's output, a FirstAgg aggregation is added for that column so
# it becomes available above the DistinctOn.
#
# Example:
#   DistinctOn(Select(input, [s = outer.s, i > 0]), aggs, priv)
#   =>
#   Select(DistinctOn(Select(input, [i > 0]), aggs', priv), [s = outer.s])
#   (where aggs' = aggs + FirstAgg(s) if s was not already projected)
[HoistSelectAboveUnorderedDistinctOn, Normalize]
(DistinctOn
    (Select $input:* $filters:*)
    $aggregations:*
    $groupingPrivate:* &
        (IsUnorderedGrouping $groupingPrivate) &
        (CanHoistCorrelatedFiltersAbove
            $filters
            (OutputCols $input)
        )
)
=>
(Select
    (DistinctOn
        (Select
            $input
            (ExtractBoundConditions $filters (OutputCols $input))
        )
        (AddFirstAggsForHoistedFilters
            $aggregations
            $filters
            (OutputCols $input)
            (GroupingOutputCols $groupingPrivate $aggregations)
        )
        $groupingPrivate
    )
    (ExtractUnboundConditions $filters (OutputCols $input))
)
```
