# Name: EliminateJoinUnderProjectLeft
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/project.opt

EliminateJoinUnderProjectLeft replaces an InnerJoin or LeftJoin with its left
input when:
1. The project doesn't use columns from the join's right input.
2. The join does not duplicate any left rows.
3. The join does not filter any left rows.

Note: EliminateJoinUnderProjectLeft should stay above EliminateProject so that
it has a chance to fire before the Project can be removed.

It is possible for references to the right input of the join to be replaced by
equivalent columns from the left input. This is handled by adding projections
that map the left column to the equivalent right column (leftCol AS rightCol).

It is ok to call MergeProjections without checking CanMergeProjections because
RemapProjectionCols returns projections that only reference columns from the
left input, and ProjectRemappedCols only projects columns from the right side
of the join.

Extracted from `project.opt` (which defines multiple rules — implement specifically `EliminateJoinUnderProjectLeft`, not the other rules in that file):

```
# EliminateJoinUnderProjectLeft replaces an InnerJoin or LeftJoin with its left
# input when:
# 1. The project doesn't use columns from the join's right input.
# 2. The join does not duplicate any left rows.
# 3. The join does not filter any left rows.
#
# Note: EliminateJoinUnderProjectLeft should stay above EliminateProject so that
# it has a chance to fire before the Project can be removed.
#
# It is possible for references to the right input of the join to be replaced by
# equivalent columns from the left input. This is handled by adding projections
# that map the left column to the equivalent right column (leftCol AS rightCol).
#
# It is ok to call MergeProjections without checking CanMergeProjections because
# RemapProjectionCols returns projections that only reference columns from the
# left input, and ProjectRemappedCols only projects columns from the right side
# of the join.
[EliminateJoinUnderProjectLeft, Normalize]
(Project
    $join:(InnerJoin | LeftJoin $left:* $right:*) &
        (JoinDoesNotDuplicateLeftRows $join) &
        (JoinPreservesLeftRows $join)
    $projections:*
    $passthrough:* &
        (CanRemapCols
            $fromCols:(UnionCols
                $passthrough
                (ProjectionOuterCols $projections)
            )
            $leftCols:(OutputCols $left)
            $fds:(FuncDeps $join)
        ) &
        (CanUseImprovedJoinElimination $fromCols $leftCols)
)
=>
(Project
    $left
    (MergeProjections
        (RemapProjectionCols $projections $leftCols $fds)
        (ProjectRemappedCols $passthrough $leftCols $fds)
        $passthrough
    )
    (DifferenceCols $passthrough (OutputCols $right))
)
```
