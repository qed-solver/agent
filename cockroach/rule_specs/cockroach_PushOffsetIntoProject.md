# Name: PushOffsetIntoProject
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/limit.opt

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
