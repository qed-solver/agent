# Name: EliminateJoinUnderGroupByLeft
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateJoinUnderGroupByLeft removes a Join operator and its right input if
it can be proven that the removal does not affect the output of the parent
grouping operator. This is the case if:

1. Only columns from the left input are being used by the grouping operator.

2. It can be proven that removal of the Join does not affect the result of the
grouping operator's aggregate functions.

3. The OrderingChoice of the grouping operator can be expressed with only
columns from the left input. Or in other words, at least one column in
every ordering group is one of the left output columns.

Condition #2 is only true when the following are all true:

1. All left rows are included in the output of the join. See the comment above
filtersMatchAllLeftRows in multiplicity_builder.go for more information on
when this is the case.
2. Either the join does not duplicate any left rows, or the join duplicates
left rows but the grouping operator's aggregate functions ignore duplicate
values. See the comment above filtersMatchLeftRowsAtMostOnce in
multiplicity_builder.go for more information on when rows are duplicated.
3. The join does not null-extend the left columns.

EliminateJoinUnderGroupByLeft should stay at the top of the file so that it
has a chance to fire before rules like EliminateDistinctOn that might prevent
matching.

Similar to the join-elimination rules that match on Project operators,
EliminateJoinUnderGroupByLeft can remap references to the right input of the
join to refer to equivalent columns on the left input.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateJoinUnderGroupByLeft`, not the other rules in that file):

```
# EliminateJoinUnderGroupByLeft removes a Join operator and its right input if
# it can be proven that the removal does not affect the output of the parent
# grouping operator. This is the case if:
#
# 1. Only columns from the left input are being used by the grouping operator.
#
# 2. It can be proven that removal of the Join does not affect the result of the
#    grouping operator's aggregate functions.
#
# 3. The OrderingChoice of the grouping operator can be expressed with only
#    columns from the left input. Or in other words, at least one column in
#    every ordering group is one of the left output columns.
#
# Condition #2 is only true when the following are all true:
#
# 1. All left rows are included in the output of the join. See the comment above
#    filtersMatchAllLeftRows in multiplicity_builder.go for more information on
#    when this is the case.
# 2. Either the join does not duplicate any left rows, or the join duplicates
#    left rows but the grouping operator's aggregate functions ignore duplicate
#    values. See the comment above filtersMatchLeftRowsAtMostOnce in
#    multiplicity_builder.go for more information on when rows are duplicated.
# 3. The join does not null-extend the left columns.
#
# EliminateJoinUnderGroupByLeft should stay at the top of the file so that it
# has a chance to fire before rules like EliminateDistinctOn that might prevent
# matching.
#
# Similar to the join-elimination rules that match on Project operators,
# EliminateJoinUnderGroupByLeft can remap references to the right input of the
# join to refer to equivalent columns on the left input.
[EliminateJoinUnderGroupByLeft, Normalize]
(GroupBy | ScalarGroupBy | DistinctOn
    $input:(InnerJoin | LeftJoin $left:*)
    $aggs:*
    $private:(GroupingPrivate $groupingCols:* $ordering:*) &
        (OrderingCanProjectCols
            $ordering
            $leftCols:(OutputCols $left)
        ) &
        (CanRemapCols
            $toRemap:(UnionCols
                $groupingCols
                (AggregationOuterCols $aggs)
            )
            $leftCols
            $fds:(FuncDeps $input)
        ) &
        (CanUseImprovedJoinElimination $toRemap $leftCols) &
        (CanEliminateJoinUnderGroupByLeft $input $aggs)
)
=>
((OpName)
    (Project
        $left
        (ProjectRemappedCols $toRemap $leftCols $fds)
        $leftCols
    )
    $aggs
    (MakeGrouping
        $groupingCols
        (PruneOrdering $ordering $leftCols)
    )
)
```
