# Name: PushSelectIntoProjectSet
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/select.opt

PushSelectIntoProjectSet pushes filters into a ProjectSet. In particular,
the filters that are bound to the input columns of the ProjectSet are
pushed down into it, in hopes of being pushed down further into joins
and scans underneath the ProjectSet.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushSelectIntoProjectSet`, not the other rules in that file):

```
# PushSelectIntoProjectSet pushes filters into a ProjectSet. In particular,
# the filters that are bound to the input columns of the ProjectSet are
# pushed down into it, in hopes of being pushed down further into joins
# and scans underneath the ProjectSet.
[PushSelectIntoProjectSet, Normalize]
(Select
    (ProjectSet $input:* $zip:*)
    $filters:[
        ...
        $item:* &
            (IsBoundBy $item $inputCols:(OutputCols $input))
        ...
    ]
)
=>
(Select
    (ProjectSet
        (Select
            $input
            (ExtractBoundConditions $filters $inputCols)
        )
        $zip
    )
    (ExtractUnboundConditions $filters $inputCols)
)
```
