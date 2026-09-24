# Name: SimplifyZeroCardinalitySemiJoin
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

SimplifyZeroCardinalitySemiJoin converts a SemiJoin operator to an empty
Values when it's known that the right input never returns any rows.

Extracted from `join.opt` (which defines multiple rules — implement specifically `SimplifyZeroCardinalitySemiJoin`, not the other rules in that file):

```
# SimplifyZeroCardinalitySemiJoin converts a SemiJoin operator to an empty
# Values when it's known that the right input never returns any rows.
[SimplifyZeroCardinalitySemiJoin, Normalize]
(SemiJoin | SemiJoinApply
    $left:*
    $right:* & (HasZeroRows $right)
)
=>
(ConstructEmptyValues (OutputCols $left))
```
