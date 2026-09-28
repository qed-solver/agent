# PruneLimitCols

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 25  **Verification rounds used:** 2

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

PruneLimitCols' correctness rests entirely on ordering semantics: that the top-$limit rows of the input under $ordering are preserved when unused columns are pruned and the ordering is pruned accordingly — and QED decides only bag (multiset) equivalence over semiring expressions, explicitly not modeling the ordering semantics of Limit/Offset/Order By (qed.pdf §6.2, and no Limit/Sort builder exists in the DSL). No faithful encoding exists as a workaround: a Limit modeled as an uninterpreted symbol/filter would be applied to different arguments on each side (full vs. pruned input, full vs. pruned ordering), which EUF-style reasoning cannot relate, and any encoding that abstracts the top-N selection into an arbitrary column-independent predicate would instead prove a mere filter/project commutation fact, not this rule. This is a fundamental limitation of QED's bag-semantic core, not a missed encoding, so the porter's UNSUPPORTED conclusion stands. ```
