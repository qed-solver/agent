# PushLimitIntoProject

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2

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

PushLimitIntoProject pushes an *ordered* Limit through a Project, and its validity rests entirely on the ordering machinery (the `OrderingCanProjectCols` guard and `PruneOrdering`), which guarantee the same rows survive on both sides; QED's bag semantics has no model for list/ordering (Sort/Limit/Offset carry no bag-semantic meaning), so with Limit treated as an uninterpreted operator the commutation `Limit(n, Project(f, R)) = Project(f, Limit(n, R))` has genuine SMT countermodels, and no DSL extension (e.g. a missing Sort/Limit builder) can supply the positional semantics to the trusted prover. The only encoding QED could certify is the vacuous identity-projection tautology, which does not port the rule's actual content, so the UNSUPPORTED conclusion is correct.
