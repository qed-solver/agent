# Name: PruneUnionAllCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneUnionAllCols prunes columns from the left and right input relations that
are never used. Since UNION ALL preserves duplicates, any column may be pruned
if it is not needed, which is not generally true of set operators.

Since UnionAll requires that both inputs have an equal number of columns,
rather than using PruneCols to prune the left and right sides, this rule
pushes down Projects on both sides to ensure that exactly the needed columns
are passed as input to the UnionAll, to prevent situations where one side has
more columns left over after PruneCols than the other (for instance, if $left
is a normal scan where all columns may be pruned, but $right is a scan with a
filter, leading to an additional column being kept on just the right side).
If extraneous, these Projects may be cleaned up later by rules like
EliminateProject.

Note: The projections could reference columns from an outer scope, e.g. due
to an apply-join or routine. We intersect with the UnionAll's output to ensure
that $needed only contains columns from the UnionAll.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneUnionAllCols`, not the other rules in that file):

```
# PruneUnionAllCols prunes columns from the left and right input relations that
# are never used. Since UNION ALL preserves duplicates, any column may be pruned
# if it is not needed, which is not generally true of set operators.
#
# Since UnionAll requires that both inputs have an equal number of columns,
# rather than using PruneCols to prune the left and right sides, this rule
# pushes down Projects on both sides to ensure that exactly the needed columns
# are passed as input to the UnionAll, to prevent situations where one side has
# more columns left over after PruneCols than the other (for instance, if $left
# is a normal scan where all columns may be pruned, but $right is a scan with a
# filter, leading to an additional column being kept on just the right side).
# If extraneous, these Projects may be cleaned up later by rules like
# EliminateProject.
#
# Note: The projections could reference columns from an outer scope, e.g. due
# to an apply-join or routine. We intersect with the UnionAll's output to ensure
# that $needed only contains columns from the UnionAll.
[PruneUnionAllCols, Normalize]
(Project
    $union:(UnionAll $left:* $right:* $colmap:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $union
            $needed:(IntersectionCols
                (UnionCols
                    (ProjectionOuterCols $projections)
                    $passthrough
                )
                (OutputCols $union)
            )
        )
)
=>
(Project
    (UnionAll
        (Project $left [] (NeededColMapLeft $needed $colmap))
        (Project $right [] (NeededColMapRight $needed $colmap))
        (PruneSetPrivate $needed $colmap)
    )
    $projections
    $passthrough
)
```
