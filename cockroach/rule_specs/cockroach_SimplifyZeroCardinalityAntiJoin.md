# Name: SimplifyZeroCardinalityAntiJoin
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

SimplifyZeroCardinalityAntiJoin converts an AntiJoin operator to an empty
Values when it's known that the right input never returns zero rows, and
there is no join condition.

Extracted from `join.opt` (which defines multiple rules — implement specifically `SimplifyZeroCardinalityAntiJoin`, not the other rules in that file):

```
# SimplifyZeroCardinalityAntiJoin converts an AntiJoin operator to an empty
# Values when it's known that the right input never returns zero rows, and
# there is no join condition.
[SimplifyZeroCardinalityAntiJoin, Normalize]
(AntiJoin | AntiJoinApply
    $left:*
    $right:* & ^(CanHaveZeroRows $right)
    []
)
=>
(ConstructEmptyValues (OutputCols $left))
```
