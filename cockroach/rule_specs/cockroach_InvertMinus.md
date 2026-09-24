# Name: InvertMinus
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/numeric.opt

InvertMinus rewrites -(a - b) to (b - a) if the operand types allow it.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `InvertMinus`, not the other rules in that file):

```
# InvertMinus rewrites -(a - b) to (b - a) if the operand types allow it.
[InvertMinus, Normalize]
(UnaryMinus
    (Minus $left:* $right:*) &
        (CanConstructBinary Minus $right $left)
)
=>
(Minus $right $left)
```
