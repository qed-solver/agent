# Name: FoldMinusZero
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/numeric.opt

FoldMinusZero folds $left - 0 for numeric types. This rule requires a check
that $left is numeric because JSON - INT is valid and is not a no-op with a
zero value.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldMinusZero`, not the other rules in that file):

```
# FoldMinusZero folds $left - 0 for numeric types. This rule requires a check
# that $left is numeric because JSON - INT is valid and is not a no-op with a
# zero value.
[FoldMinusZero, Normalize]
(Minus $left:(IsAdditiveType (TypeOf $left)) $right:(Const 0))
=>
(Cast $left (BinaryType Minus $left $right))
```
