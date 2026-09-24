# Name: EliminateAntiJoin
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

EliminateAntiJoin discards an AntiJoin operator when it's known that the right
input never returns any rows.

Extracted from `join.opt` (which defines multiple rules — implement specifically `EliminateAntiJoin`, not the other rules in that file):

```
# EliminateAntiJoin discards an AntiJoin operator when it's known that the right
# input never returns any rows.
[EliminateAntiJoin, Normalize]
(AntiJoin | AntiJoinApply
    $left:*
    $right:* & (HasZeroRows $right)
)
=>
$left
```
