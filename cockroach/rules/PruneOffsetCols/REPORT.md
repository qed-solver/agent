# PruneOffsetCols

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

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
```

## Independent verifier review

**Verdict:** AGREE

PruneOffsetCols' soundness rests entirely on order-dependent row selection — that pruning columns not referenced by the ordering or outer projection leaves the ordered row sequence (and hence which rows OFFSET k skips) unchanged, including tie-break behavior — and QED's semiring/bag theory has no Sort/Limit/Offset construct, no row ordering or identity (all virtual types map to uninterpreted integer columns), and no axiom that could relate Offset(R) to Offset(π(R)). Adding an Offset builder to RelRN cannot close the gap, because the unmodifiable Rust prover has no ordering semantics to interpret it with (per qed.pdf §6.2), so this is a genuine QED limitation rather than a missing DSL capability — consistent with the verified sibling skips of EliminateOffset and PushProjectionThroughLimitAndOffset. ```
