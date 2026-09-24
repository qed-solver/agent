# Name: PruneWithScanCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneWithScanCols discards columns scanned from the WithScan that are never
used.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneWithScanCols`, not the other rules in that file):

```
# PruneWithScanCols discards columns scanned from the WithScan that are never
# used.
[PruneWithScanCols, Normalize]
(Project
    $input:(WithScan)
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
