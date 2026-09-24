# Name: EliminateJoinUnderGroupByRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateJoinUnderGroupByRight is symmetric with
EliminateJoinUnderGroupByLeft, except that it matches on InnerJoins.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateJoinUnderGroupByRight`, not the other rules in that file):

```
# EliminateJoinUnderGroupByRight is symmetric with
# EliminateJoinUnderGroupByLeft, except that it matches on InnerJoins.
[EliminateJoinUnderGroupByRight, Normalize]
(GroupBy | ScalarGroupBy | DistinctOn
    $input:(InnerJoin * $right:*)
    $aggs:*
    $private:(GroupingPrivate $groupingCols:* $ordering:*) &
        (OrderingCanProjectCols
            $ordering
            $rightCols:(OutputCols $right)
        ) &
        (CanRemapCols
            $toRemap:(UnionCols
                $groupingCols
                (AggregationOuterCols $aggs)
            )
            $rightCols
            $fds:(FuncDeps $input)
        ) &
        (CanUseImprovedJoinElimination $toRemap $rightCols) &
        (CanEliminateJoinUnderGroupByRight $input $aggs)
)
=>
((OpName)
    (Project
        $right
        (ProjectRemappedCols $toRemap $rightCols $fds)
        $rightCols
    )
    $aggs
    (MakeGrouping
        $groupingCols
        (PruneOrdering $ordering $rightCols)
    )
)
```
