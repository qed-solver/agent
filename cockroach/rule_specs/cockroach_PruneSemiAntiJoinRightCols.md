# Name: PruneSemiAntiJoinRightCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneSemiAntiJoinRightCols discards columns on the right side of a
Semi or Anti join that are never used. This is similar to PruneJoinRightCols.
PruneJoinRightCols normally prunes the RHS of a join but it can't do that
in the case of Semi/Anti joins because the projection is eliminated after
the LHS is pruned. This rule doesn't require a projection over the Semi/Anti
join in order to prune the RHS.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneSemiAntiJoinRightCols`, not the other rules in that file):

```
# PruneSemiAntiJoinRightCols discards columns on the right side of a
# Semi or Anti join that are never used. This is similar to PruneJoinRightCols.
# PruneJoinRightCols normally prunes the RHS of a join but it can't do that
# in the case of Semi/Anti joins because the projection is eliminated after
# the LHS is pruned. This rule doesn't require a projection over the Semi/Anti
# join in order to prune the RHS.
[PruneSemiAntiJoinRightCols, Normalize]
(SemiJoin | SemiJoinApply | AntiJoin | AntiJoinApply
    $left:*
    $right:*
    $on:*
    $private:* &
        (CanPruneCols $right $needed:(FilterOuterCols $on))
)
=>
((OpName) $left (PruneCols $right $needed) $on $private)
```
