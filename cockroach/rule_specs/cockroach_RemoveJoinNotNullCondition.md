# Name: RemoveJoinNotNullCondition
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

RemoveJoinNotNullCondition removes a filter with an IS NOT NULL condition when
the given column has a NOT NULL constraint. Only left joins and full joins are
matched because filters can be pushed down from the ON conditions of inner and
semi joins.

Extracted from `join.opt` (which defines multiple rules — implement specifically `RemoveJoinNotNullCondition`, not the other rules in that file):

```
# RemoveJoinNotNullCondition removes a filter with an IS NOT NULL condition when
# the given column has a NOT NULL constraint. Only left joins and full joins are
# matched because filters can be pushed down from the ON conditions of inner and
# semi joins.
[RemoveJoinNotNullCondition, Normalize]
(LeftJoin | FullJoin
    $left:*
    $right:*
    $on:[
        ...
        $item:(FiltersItem
            (IsNot
                (Variable
                    $col:* & (IsColNotNull2 $col $left $right)
                )
                (Null)
            )
        )
        ...
    ]
    $private:*
)
=>
((OpName) $left $right (RemoveFiltersItem $on $item) $private)
```
