# Name: FoldMultOne
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/numeric.opt

FoldMultOne folds $left * 1 for numeric types.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldMultOne`, not the other rules in that file):

```
# FoldMultOne folds $left * 1 for numeric types.
[FoldMultOne, Normalize]
(Mult $left:* $right:(Const 1))
=>
(Cast $left (BinaryType Mult $left $right))
```
