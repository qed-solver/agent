# Name: PushSelectIntoProject
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/select.opt

PushSelectIntoProject pushes the Select operator into its Project input. This
is typically preferable because it minimizes the number of rows which Project
needs to process. This is especially important if Project is adding expensive
computed columns.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushSelectIntoProject`, not the other rules in that file):

```
# PushSelectIntoProject pushes the Select operator into its Project input. This
# is typically preferable because it minimizes the number of rows which Project
# needs to process. This is especially important if Project is adding expensive
# computed columns.
[PushSelectIntoProject, Normalize]
(Select
    (Project $input:* $projections:* $passthrough:*)
    $filters:[
        ...
        $item:* &
            (IsBoundBy $item $inputCols:(OutputCols $input))
        ...
    ]
)
=>
(Select
    (Project
        (Select
            $input
            (ExtractBoundConditions $filters $inputCols)
        )
        $projections
        $passthrough
    )
    (ExtractUnboundConditions $filters $inputCols)
)
```
