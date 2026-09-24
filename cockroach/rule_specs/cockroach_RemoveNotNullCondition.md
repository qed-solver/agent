# Name: RemoveNotNullCondition
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/select.opt

RemoveNotNullCondition removes a filter with an IS NOT NULL condition
when the given column has a NOT NULL constraint.

Extracted from `select.opt` (which defines multiple rules — implement specifically `RemoveNotNullCondition`, not the other rules in that file):

```
# RemoveNotNullCondition removes a filter with an IS NOT NULL condition
# when the given column has a NOT NULL constraint.
[RemoveNotNullCondition, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (IsNot
                (Variable $col:* & (IsColNotNull $col $input))
                (Null)
            )
        )
        ...
    ]
)
=>
(Select $input (RemoveFiltersItem $filters $item))
```
