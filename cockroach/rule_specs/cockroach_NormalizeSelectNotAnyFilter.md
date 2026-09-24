# Name: NormalizeSelectNotAnyFilter
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

NormalizeSelectNotAnyFilter rewrites a Not Any expression that is a top-level
conjunct in Select filters, turning it into a Not Exists expression. Not Any
can be rewritten as Not Exists in this context because a NULL return value is
treated as False by the filter.

Not Exists is more efficient than Not Any, since its null handling is much
simpler. In addition, the Not Exists can be transformed into an anti-join.

Citations: [5] (section 3.5)

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `NormalizeSelectNotAnyFilter`, not the other rules in that file):

```
# NormalizeSelectNotAnyFilter rewrites a Not Any expression that is a top-level
# conjunct in Select filters, turning it into a Not Exists expression. Not Any
# can be rewritten as Not Exists in this context because a NULL return value is
# treated as False by the filter.
#
# Not Exists is more efficient than Not Any, since its null handling is much
# simpler. In addition, the Not Exists can be transformed into an anti-join.
#
# Citations: [5] (section 3.5)
[NormalizeSelectNotAnyFilter, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Not (Any $anyInput:* $scalar:* $anyPrivate:*))
        )
        ...
    ]
)
=>
(Select
    $input
    (ReplaceFiltersItem
        $filters
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
)
```
