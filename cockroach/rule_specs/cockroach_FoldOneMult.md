# Name: FoldOneMult
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/numeric.opt

FoldOneMult folds 1 * $right for numeric types.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldOneMult`, not the other rules in that file):

```
# FoldOneMult folds 1 * $right for numeric types.
[FoldOneMult, Normalize]
(Mult $left:(Const 1) $right:*)
=>
(Cast $right (BinaryType Mult $left $right))
```
