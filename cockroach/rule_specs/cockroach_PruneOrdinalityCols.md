# Name: PruneOrdinalityCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneOrdinalityCols discards Ordinality input columns that are never used.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneOrdinalityCols`, not the other rules in that file):

```
# PruneOrdinalityCols discards Ordinality input columns that are never used.
[PruneOrdinalityCols, Normalize]
(Project
    (Ordinality $input:* $ordinalityPrivate:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $input
            $needed:(UnionCols3
                (NeededOrdinalityCols $ordinalityPrivate)
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    (Ordinality
        (PruneCols $input $needed)
        (PruneOrderingOrdinality $ordinalityPrivate $needed)
    )
    $projections
    $passthrough
)
```
