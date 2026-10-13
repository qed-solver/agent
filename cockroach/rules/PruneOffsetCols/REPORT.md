# PruneOffsetCols

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2

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

PruneOffsetCols's only non-trivial content is pushing a column-pruning Project below an Offset, whose semantics is positional row skipping under a specified ordering, and QED's bag-semantic core assigns no meaning to Sort/Limit/Offset (the DSL and serializer expose no Offset node the prover could interpret). Modeling Offset as an uninterpreted bag-to-bag function would not rescue it either, since Project(Offset(R)) = Project(Offset(prune(R))) fails for arbitrary such functions, so the rule's correctness is irreducibly about order-based row selection. Hence no expressible special case (offset = 0, unique ordering key, etc.) leaves a genuine rule about Offset, making this a fundamental QED limitation rather than a missed encoding — consistent with the earlier AGREE on the structurally identical PruneLimitCols. ```
