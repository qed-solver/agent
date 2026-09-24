# Name: NormalizeJoinNotAnyFilter
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

NormalizeJoinNotAnyFilter is similar to NormalizeSelectNotAnyFilter, except
that it operates on Not Any expressions within Join filters rather than Select
filters.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `NormalizeJoinNotAnyFilter`, not the other rules in that file):

```
# NormalizeJoinNotAnyFilter is similar to NormalizeSelectNotAnyFilter, except
# that it operates on Not Any expressions within Join filters rather than Select
# filters.
[NormalizeJoinNotAnyFilter, Normalize]
(Join
    $left:*
    $right:*
    $on:[
        ...
        $item:(FiltersItem
            (Not (Any $anyInput:* $scalar:* $anyPrivate:*))
        )
        ...
    ]
    $private:*
)
=>
((OpName)
    $left
    $right
    (ReplaceFiltersItem
        $on
        $item
        (Not
            (Exists
                (Select
                    $anyInput
                    [
                        (FiltersItem
                            (IsNot
                                (ConstructAnyCondition
                                    $anyInput
                                    $scalar
                                    $anyPrivate
                                )
                                (False)
                            )
                        )
                    ]
                )
                (ConvertSubToExistsPrivate $anyPrivate)
            )
        )
    )
    $private
)
```
