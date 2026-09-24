# Name: PruneOffsetCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneOffsetCols discards Offset input columns that are never used.

The PruneCols property should prevent this rule (which pushes Project below
Offset) from cycling with the PushOffsetIntoProject rule (which pushes Offset
below Project).

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneOffsetCols`, not the other rules in that file):

```
# PruneOffsetCols discards Offset input columns that are never used.
#
# The PruneCols property should prevent this rule (which pushes Project below
# Offset) from cycling with the PushOffsetIntoProject rule (which pushes Offset
# below Project).
[PruneOffsetCols, Normalize]
(Project
    (Offset $input:* $offset:* $ordering:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $input
            $needed:(UnionCols3
                (OrderingCols $ordering)
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    (Offset
        (PruneCols $input $needed)
        $offset
        (PruneOrdering $ordering $needed)
    )
    $projections
    $passthrough
)
```
