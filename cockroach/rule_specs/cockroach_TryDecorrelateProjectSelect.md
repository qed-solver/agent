# Name: TryDecorrelateProjectSelect
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateProjectSelect tries to decorrelate by hoisting a Select operator
that sits below a LeftJoin/Project operator combo. The Project operator itself
can't be reordered above the LeftJoin like it can in the InnerJoin case.
However, the Select filter can be merged with the LeftJoin filter, which is
enough to decorrelate in several useful cases.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateProjectSelect`, not the other rules in that file):

```
# TryDecorrelateProjectSelect tries to decorrelate by hoisting a Select operator
# that sits below a LeftJoin/Project operator combo. The Project operator itself
# can't be reordered above the LeftJoin like it can in the InnerJoin case.
# However, the Select filter can be merged with the LeftJoin filter, which is
# enough to decorrelate in several useful cases.
[TryDecorrelateProjectSelect, Normalize]
(LeftJoinApply
    $left:*
    $right:(Project
        (Select
            $selectInput:*
            $filters:* &
                ^(FiltersBoundBy
                    $filters
                    (OutputCols $selectInput)
                )
        )
        $projections:*
        $passthrough:*
    )
    $on:*
    $private:*
)
=>
(Project
    ((OpName)
        $left
        (Project
            $selectInput
            $projections
            (UnionCols $passthrough (OutputCols $selectInput))
        )
        (ConcatFilters $on $filters)
        $private
    )
    []
    (OutputCols2 $left $right)
)
```
