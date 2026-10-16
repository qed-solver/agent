# PushLimitIntoProject

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 6  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/limit.opt

PushLimitIntoProject pushes the Limit operator into its Project input. It is
desirable to push the Limit operator as low in the query as possible, in order
to minimize the number of rows that other operators need to process.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `PushLimitIntoProject`, not the other rules in that file):

```
# PushLimitIntoProject pushes the Limit operator into its Project input. It is
# desirable to push the Limit operator as low in the query as possible, in order
# to minimize the number of rows that other operators need to process.
[PushLimitIntoProject, Normalize]
(Limit
    (Project $input:* $projections:* $passthrough:*)
    $limit:*
    $ordering:* &
        (OrderingCanProjectCols
            $ordering
            $cols:(OutputCols $input)
        )
)
=>
(Project
    (Limit $input $limit (PruneOrdering $ordering $cols))
    $projections
    $passthrough
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests entirely on Limit's ordered-prefix semantics plus its side condition `OrderingCanProjectCols`: the top-N rows under an ordering that must be referable only through input columns have to survive projection unchanged. QED operates purely under bag semantics and has no model of Limit/Sort/Offset ordering at all (Limit is at best an uninterpreted QOp whose output is unconstrained by its input, under which L(π(R)) ≡ π(L(R)) is already false — rows that merge under π across the cut point yield different bags), and RRule's before/after pair has no mechanism to state the `OrderingCanProjectCols` guard, so the only provable encoding is the vacuous identity-projection degenerate. ```
