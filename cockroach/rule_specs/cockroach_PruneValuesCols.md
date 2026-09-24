# Name: PruneValuesCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneValuesCols discards Values columns that are never used.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneValuesCols`, not the other rules in that file):

```
# PruneValuesCols discards Values columns that are never used.
[PruneValuesCols, Normalize]
(Project
    $input:(Values)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $input
            $needed:(UnionCols
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project (PruneCols $input $needed) $projections $passthrough)
```
