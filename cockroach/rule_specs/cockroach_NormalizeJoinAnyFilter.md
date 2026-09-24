# Name: NormalizeJoinAnyFilter
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

NormalizeJoinAnyFilter is similar to NormalizeSelectAnyFilter, except that it
operates on Any expressions within Join filters rather than Select filters.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `NormalizeJoinAnyFilter`, not the other rules in that file):

```
# NormalizeJoinAnyFilter is similar to NormalizeSelectAnyFilter, except that it
# operates on Any expressions within Join filters rather than Select filters.
[NormalizeJoinAnyFilter, Normalize]
(Join
    $left:*
    $right:*
    $on:[
        ...
        $item:(FiltersItem
            (Any $anyInput:* $scalar:* $anyPrivate:*)
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
        (Exists
            (Select
                $anyInput
                [
                    (FiltersItem
                        (ConstructAnyCondition
                            $anyInput
                            $scalar
                            $anyPrivate
                        )
                    )
                ]
            )
            (ConvertSubToExistsPrivate $anyPrivate)
        )
    )
    $private
)
```
