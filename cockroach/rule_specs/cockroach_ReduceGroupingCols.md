# Name: ReduceGroupingCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/groupby.opt

ReduceGroupingCols eliminates redundant grouping columns from the GroupBy
operator and replaces them by ConstAgg aggregate functions. A grouping
column is redundant if it is functionally determined by the other grouping
columns. If that's true, then its value must be constant within a group.
Therefore, it has no effect on the grouping and can instead be represented as
an ConstAgg aggregate, since all rows in the group have the same value for
that column.

Note: Doesn't match EnsureDistinctOn because test cases were too difficult to
find. If a test case for EnsureDistinctOn is found, it should be added to the
match pattern.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `ReduceGroupingCols`, not the other rules in that file):

```
# ReduceGroupingCols eliminates redundant grouping columns from the GroupBy
# operator and replaces them by ConstAgg aggregate functions. A grouping
# column is redundant if it is functionally determined by the other grouping
# columns. If that's true, then its value must be constant within a group.
# Therefore, it has no effect on the grouping and can instead be represented as
# an ConstAgg aggregate, since all rows in the group have the same value for
# that column.
#
# Note: Doesn't match EnsureDistinctOn because test cases were too difficult to
# find. If a test case for EnsureDistinctOn is found, it should be added to the
# match pattern.
[ReduceGroupingCols, Normalize]
(GroupBy | DistinctOn
    $input:*
    $aggregations:*
    $groupingPrivate:* &
        ^(ColsAreEmpty
            $redundantCols:(RedundantCols
                $input
                (GroupingCols $groupingPrivate)
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
