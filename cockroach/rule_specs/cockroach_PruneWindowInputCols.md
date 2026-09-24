# Name: PruneWindowInputCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneWindowInputCols discards window passthrough columns which are never used.
NB: This rule should go after PruneWindowOutputCols, or else this rule can get
into a cycle.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneWindowInputCols`, not the other rules in that file):

```
# PruneWindowInputCols discards window passthrough columns which are never used.
# NB: This rule should go after PruneWindowOutputCols, or else this rule can get
# into a cycle.
[PruneWindowInputCols, Normalize]
(Project
    $input:(Window $innerInput:* $fn:* $private:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $input
            $needed:(UnionCols3
                (NeededWindowCols $fn $private)
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    (Window (PruneCols $innerInput $needed) $fn $private)
    $projections
    $passthrough
)
```
