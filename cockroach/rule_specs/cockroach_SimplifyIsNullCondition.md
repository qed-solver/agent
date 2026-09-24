# Name: SimplifyIsNullCondition
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/select.opt

SimplifyIsNullCondition replaces a filter with an IS NULL condition with
False when the given column has a NOT NULL constraint.

Extracted from `select.opt` (which defines multiple rules — implement specifically `SimplifyIsNullCondition`, not the other rules in that file):

```
# SimplifyIsNullCondition replaces a filter with an IS NULL condition with
# False when the given column has a NOT NULL constraint.
[SimplifyIsNullCondition, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Is
                (Variable $col:* & (IsColNotNull $col $input))
                (Null)
            )
        )
        ...
    ]
)
=>
(Select $input [ (FiltersItem (False)) ])
```
