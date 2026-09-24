# Name: FoldFloorDivOne
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/numeric.opt

FoldFloorDivOne folds $left // 1 for integer types.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldFloorDivOne`, not the other rules in that file):

```
# FoldFloorDivOne folds $left // 1 for integer types.
[FoldFloorDivOne, Normalize]
(FloorDiv $left:* $right:(Const 1) & (IsInt $left))
=>
(Cast $left (BinaryType (OpName) $left $right))
```
