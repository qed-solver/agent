# Name: NormalizeSelectAnyFilter
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

NormalizeSelectAnyFilter rewrites an Any expression that is a top-level
conjunct in Select filters, turning it into an Exists expression. Any can be
rewritten as Exists in this context because a NULL return value is treated as
False by the filter.

Exists is more efficient than Any, since its null handling is much simpler. In
addition, the Exists can be transformed into a semi-join.

Citations: [5] (section 3.5)

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `NormalizeSelectAnyFilter`, not the other rules in that file):

```
# NormalizeSelectAnyFilter rewrites an Any expression that is a top-level
# conjunct in Select filters, turning it into an Exists expression. Any can be
# rewritten as Exists in this context because a NULL return value is treated as
# False by the filter.
#
# Exists is more efficient than Any, since its null handling is much simpler. In
# addition, the Exists can be transformed into a semi-join.
#
# Citations: [5] (section 3.5)
[NormalizeSelectAnyFilter, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Any $anyInput:* $scalar:* $anyPrivate:*)
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
)
```
