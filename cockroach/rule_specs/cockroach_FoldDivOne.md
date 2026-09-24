# Name: FoldDivOne
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/numeric.opt

FoldDivOne folds $left / 1 for numeric types.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldDivOne`, not the other rules in that file):

```
# FoldDivOne folds $left / 1 for numeric types.
[FoldDivOne, Normalize]
(Div $left:* $right:(Const 1))
=>
(Cast $left (BinaryType (OpName) $left $right))
```
