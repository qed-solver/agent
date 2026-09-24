# Name: PruneProjectCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneProjectCols discards columns from a nested project which are not used by
the outer project.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneProjectCols`, not the other rules in that file):

```
# PruneProjectCols discards columns from a nested project which are not used by
# the outer project.
[PruneProjectCols, Normalize]
(Project
    $project:(Project)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $project
            $needed:(UnionCols
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project (PruneCols $project $needed) $projections $passthrough)
```
