# Name: PushSelectIntoOrdinality
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/select.opt

PushSelectIntoOrdinality pushes the Select operator into its Ordinality input
if the Ordinality operation was built for the purposes of removing duplicate
rows, and the actual values returned by the Ordinality operation don't matter.
This is typically preferable because it allows the Select to be pushed into
operations beneath the Ordinality, minimizing the number of rows subsequent
operations need to process and potentially pushing the Select down far enough
to enable constrained scans. This may also enable other normalization rules
which match on Select expressions to fire.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushSelectIntoOrdinality`, not the other rules in that file):

```
# PushSelectIntoOrdinality pushes the Select operator into its Ordinality input
# if the Ordinality operation was built for the purposes of removing duplicate
# rows, and the actual values returned by the Ordinality operation don't matter.
# This is typically preferable because it allows the Select to be pushed into
# operations beneath the Ordinality, minimizing the number of rows subsequent
# operations need to process and potentially pushing the Select down far enough
# to enable constrained scans. This may also enable other normalization rules
# which match on Select expressions to fire.
[PushSelectIntoOrdinality, Normalize]
(Select
    (Ordinality $input:* $private:*) &
        (ForDuplicateRemoval $private)
    $filters:[
        ...
        $item:* &
            (IsBoundBy $item $inputCols:(OutputCols $input))
        ...
    ]
)
=>
(Select
    (Ordinality
        (Select
            $input
            (ExtractBoundConditions $filters $inputCols)
        )
        $private
    )
    (ExtractUnboundConditions $filters $inputCols)
)
```
