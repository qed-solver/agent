# Name: PushSelectIntoWindow
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/window.opt

PushSelectIntoWindow pushes down a Select which can be satisfied by only the
functional closure of the columns being partitioned over. This is valid
because it's "all-or-nothing" - we only entirely eliminate a partition or
don't eliminate it at all.

Extracted from `window.opt` (which defines multiple rules — implement specifically `PushSelectIntoWindow`, not the other rules in that file):

```
# PushSelectIntoWindow pushes down a Select which can be satisfied by only the
# functional closure of the columns being partitioned over. This is valid
# because it's "all-or-nothing" - we only entirely eliminate a partition or
# don't eliminate it at all.
[PushSelectIntoWindow, Normalize]
(Select
    (Window $input:* $fn:* $private:*)
    $filters:[
        ...
        $item:* &
            (ColsAreDeterminedBy
                (OuterCols $item)
                $partitionCols:(WindowPartition $private)
                $input
            )
        ...
    ]
)
=>
(Select
    (Window
        (Select
            $input
            (ExtractDeterminedConditions
                $filters
                $partitionCols
                $input
            )
        )
        $fn
        $private
    )
    (ExtractUndeterminedConditions
        $filters
        $partitionCols
        $input
    )
)
```
