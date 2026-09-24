# Name: FoldZeroPlus
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/numeric.opt

FoldZeroPlus folds 0 + $right for numeric types.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldZeroPlus`, not the other rules in that file):

```
# FoldZeroPlus folds 0 + $right for numeric types.
[FoldZeroPlus, Normalize]
(Plus $left:(Const 0) $right:*)
=>
(Cast $right (BinaryType Plus $left $right))
```
