# Name: PruneJoinLeftCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneJoinLeftCols discards columns on the left side of a join that are never
used. AddDerivedOnClauseConditionsFromFKContraints builds equijoin predicates
which might be added during optimization, if any, to ensure those columns are
not pruned away.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneJoinLeftCols`, not the other rules in that file):

```
# PruneJoinLeftCols discards columns on the left side of a join that are never
# used. AddDerivedOnClauseConditionsFromFKContraints builds equijoin predicates
# which might be added during optimization, if any, to ensure those columns are
# not pruned away.
[PruneJoinLeftCols, Normalize]
(Project
    $input:(Join $left:* $right:* $on:* $private:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $left
            $needed:(UnionCols4
                (OuterCols $right)
                (FilterOuterCols
                    (AddDerivedOnClauseConditionsFromFKContraints
                        $on
                        $left
                        $right
                    )
                )
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    ((OpName $input)
        (PruneCols $left $needed)
        $right
        $on
        $private
    )
    $projections
    $passthrough
)
```
