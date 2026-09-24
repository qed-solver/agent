# Name: PruneScanCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneScanCols discards Scan operator columns that are never used. The needed
columns are pushed down into the Scan's opt.ScanOpDef private.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneScanCols`, not the other rules in that file):

```
# PruneScanCols discards Scan operator columns that are never used. The needed
# columns are pushed down into the Scan's opt.ScanOpDef private.
[PruneScanCols, Normalize]
(Project
    $input:(Scan)
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
