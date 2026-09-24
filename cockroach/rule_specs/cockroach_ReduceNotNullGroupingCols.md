# Name: ReduceNotNullGroupingCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/groupby.opt

ReduceNotNullGroupingCols is similar to ReduceGroupingCols, but with the
additional restriction that nullable columns cannot be removed from the set of
grouping columns. This is because the UpsertDistinctOn operator treats NULL
values as not equal to one another, and therefore will not group them
together. Since removing a grouping column is equivalent to grouping all
values of that column together, this would be incorrect in the case where all
input rows are NULL for that column:

SELECT c FROM t WHERE c IS NULL

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `ReduceNotNullGroupingCols`, not the other rules in that file):

```
# ReduceNotNullGroupingCols is similar to ReduceGroupingCols, but with the
# additional restriction that nullable columns cannot be removed from the set of
# grouping columns. This is because the UpsertDistinctOn operator treats NULL
# values as not equal to one another, and therefore will not group them
# together. Since removing a grouping column is equivalent to grouping all
# values of that column together, this would be incorrect in the case where all
# input rows are NULL for that column:
#
#   SELECT c FROM t WHERE c IS NULL
#
[ReduceNotNullGroupingCols, Normalize]
(UpsertDistinctOn | EnsureUpsertDistinctOn
    $input:*
    $aggregations:*
    $groupingPrivate:* &
        ^(ColsAreEmpty
            $redundantCols:(IntersectionCols
                (RedundantCols
                    $input
                    (GroupingCols $groupingPrivate)
                )
                (NotNullCols $input)
            )
        )
)
=>
((OpName)
    $input
    (AppendAggCols $aggregations ConstAgg $redundantCols)
    (RemoveGroupingCols $groupingPrivate $redundantCols)
)
```
