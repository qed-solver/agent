# Name: PushLimitIntoProject
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/limit.opt

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
