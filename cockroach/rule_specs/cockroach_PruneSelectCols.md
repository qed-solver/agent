# Name: PruneSelectCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneSelectCols discards Select input columns that are never used.

The PruneCols property should prevent this rule (which pushes Project below
Select) from cycling with the PushSelectIntoProject rule (which pushes Select
below Project).

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneSelectCols`, not the other rules in that file):

```
# PruneSelectCols discards Select input columns that are never used.
#
# The PruneCols property should prevent this rule (which pushes Project below
# Select) from cycling with the PushSelectIntoProject rule (which pushes Select
# below Project).
[PruneSelectCols, Normalize]
(Project
    (Select $input:* $filters:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $input
            $needed:(UnionCols3
                (FilterOuterCols $filters)
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    (Select (PruneCols $input $needed) $filters)
    $projections
    $passthrough
)
```
