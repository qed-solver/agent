# Name: PruneJoinRightCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneJoinRightCols discards columns on the right side of a join that are never
used. AddDerivedOnClauseConditionsFromFKContraints builds equijoin predicates
which might be added during optimization, if any, to ensure those columns are
not pruned away.

The PruneCols property should prevent this rule (which pushes Project below
Join) from cycling with the TryDecorrelateProject rule (which pushes Join
below Project).

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneJoinRightCols`, not the other rules in that file):

```
# PruneJoinRightCols discards columns on the right side of a join that are never
# used. AddDerivedOnClauseConditionsFromFKContraints builds equijoin predicates
# which might be added during optimization, if any, to ensure those columns are
# not pruned away.
#
# The PruneCols property should prevent this rule (which pushes Project below
# Join) from cycling with the TryDecorrelateProject rule (which pushes Join
# below Project).
[PruneJoinRightCols, Normalize]
(Project
    $input:(Join $left:* $right:* $on:* $private:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $right
            $needed:(UnionCols3
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
        $left
        (PruneCols $right $needed)
        $on
        $private
    )
    $projections
    $passthrough
)
```
