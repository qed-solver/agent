# Name: PruneProjectSetCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneProjectSetCols discards ProjectSet columns that are never used.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneProjectSetCols`, not the other rules in that file):

```
# PruneProjectSetCols discards ProjectSet columns that are never used.
[PruneProjectSetCols, Normalize]
(Project
    $input:(ProjectSet $innerInput:* $zip:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $input
            $needed:(UnionCols3
                (ZipOuterCols $zip)
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    (ProjectSet (PruneCols $innerInput $needed) $zip)
    $projections
    $passthrough
)
```
