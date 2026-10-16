# PruneLimitCols

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 41  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

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
```

## Independent verifier review

**Verdict:** AGREE

PruneLimitCols's correctness rests entirely on Sort/Limit ordering semantics — specifically that, with all ordering columns retained, the top-N selection on the pruned input is bag-equivalent (modulo tie nondeterminism) to the top-N selection on the full input, followed by the outer projection. QED's bag-semantic model has no notion of row order and treats Sort/Limit/Offset as uninterpreted opaque operations, so the SMT solver cannot relate the two distinct Limit applications (one over R, one over pruned(R)) appearing in before() and after(); extending the DSL with Sort/Limit builders (even though JSONSerializer already emits sort/limit JSON) would not close this gap, and the only provable residue — the trivial projection composition π_D(R) ≡ π_D(π_needed(R)) with the Limit deleted — no longer contains the Limit operator that defines the rule. ```
