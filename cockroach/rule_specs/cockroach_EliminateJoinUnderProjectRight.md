# Name: EliminateJoinUnderProjectRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/project.opt

EliminateJoinUnderProjectRight mirrors EliminateJoinUnderProjectLeft, except
that it only matches InnerJoins.

Extracted from `project.opt` (which defines multiple rules — implement specifically `EliminateJoinUnderProjectRight`, not the other rules in that file):

```
# EliminateJoinUnderProjectRight mirrors EliminateJoinUnderProjectLeft, except
# that it only matches InnerJoins.
[EliminateJoinUnderProjectRight, Normalize]
(Project
    $join:(InnerJoin $left:* $right:*) &
        (JoinDoesNotDuplicateRightRows $join) &
        (JoinPreservesRightRows $join)
    $projections:*
    $passthrough:* &
        (CanRemapCols
            $fromCols:(UnionCols
                $passthrough
                (ProjectionOuterCols $projections)
            )
            $rightCols:(OutputCols $right)
            $fds:(FuncDeps $join)
        ) &
        (CanUseImprovedJoinElimination $fromCols $rightCols)
)
=>
(Project
    $right
    (MergeProjections
        (RemapProjectionCols $projections $rightCols $fds)
        (ProjectRemappedCols $passthrough $rightCols $fds)
        $passthrough
    )
    (DifferenceCols $passthrough (OutputCols $left))
)
```
