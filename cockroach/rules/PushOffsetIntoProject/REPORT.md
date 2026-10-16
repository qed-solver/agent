# PushOffsetIntoProject

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 8  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/limit.opt

PushOffsetIntoProject pushes the Offset operator into its Project input. It is
desirable to push the Offset operator as low in the query as possible, in
order to minimize the number of rows that other operators need to process.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `PushOffsetIntoProject`, not the other rules in that file):

```
# PushOffsetIntoProject pushes the Offset operator into its Project input. It is
# desirable to push the Offset operator as low in the query as possible, in
# order to minimize the number of rows that other operators need to process.
[PushOffsetIntoProject, Normalize]
(Offset
    (Project $input:* $projections:* $passthrough:*)
    $offset:*
    $ordering:* &
        (OrderingCanProjectCols
            $ordering
            $cols:(OutputCols $input)
        )
)
=>
(Project
    (Offset $input $offset (PruneOrdering $ordering $cols))
    $projections
    $passthrough
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests entirely on row order — Offset means "skip the first k rows in $ordering", and the OrderingCanProjectCols/PruneOrdering side conditions exist precisely to guarantee the same rows are kept on both sides — while QED decides equivalence purely under bag semantics with no interpretation for Sort/Limit/Offset (qed.pdf §6.2), operators the DSL also does not expose. No non-vacuous encoding rescues it: an uninterpreted offset symbol carries no order-preservation facts the prover could use to commute it past a projection, and any concrete offset model buildable from the core language (e.g., a count-based filter over a key column) is only valid for identity/monotone projections that the DSL cannot even state, leaving only the vacuous degenerate. (The porter's specific "non-injective projection merges rows" argument was actually wrong — a pointwise projection does commute with tail-skipping as multisets — but the conclusion is correct for the ordering-semantics reason above.)
