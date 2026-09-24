# Name: PruneLimitCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneLimitCols discards Limit input columns that are never used.

The PruneCols property should prevent this rule (which pushes Project below
Limit) from cycling with the PushLimitIntoProject rule (which pushes Limit
below Project).

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneLimitCols`, not the other rules in that file):

```
# PruneLimitCols discards Limit input columns that are never used.
#
# The PruneCols property should prevent this rule (which pushes Project below
# Limit) from cycling with the PushLimitIntoProject rule (which pushes Limit
# below Project).
[PruneLimitCols, Normalize]
(Project
    (Limit $input:* $limit:* $ordering:*)
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
    (Limit
        (PruneCols $input $needed)
        $limit
        (PruneOrdering $ordering $needed)
    )
    $projections
    $passthrough
)
```
