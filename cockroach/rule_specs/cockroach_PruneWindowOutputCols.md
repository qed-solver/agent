# Name: PruneWindowOutputCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneWindowOutputCols eliminates unused window functions from a Window
expression.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneWindowOutputCols`, not the other rules in that file):

```
# PruneWindowOutputCols eliminates unused window functions from a Window
# expression.
[PruneWindowOutputCols, Normalize]
(Project
    (Window $input:* $windows:* $private:*)
    $projections:*
    $passthrough:* &
        (CanPruneWindows
            $needed:(UnionCols
                (ProjectionOuterCols $projections)
                $passthrough
            )
            $windows
        )
)
=>
(Project
    (Window $input (PruneWindows $needed $windows) $private)
    $projections
    $passthrough
)
```
