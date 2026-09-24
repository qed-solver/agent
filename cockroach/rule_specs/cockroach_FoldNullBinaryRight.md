# Name: FoldNullBinaryRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldNullBinaryRight replaces the binary operator with null if its right input
is null and it does not allow null arguments.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldNullBinaryRight`, not the other rules in that file):

```
# FoldNullBinaryRight replaces the binary operator with null if its right input
# is null and it does not allow null arguments.
[FoldNullBinaryRight, Normalize]
(Binary
    $left:*
    $right:(Null) & ^(AllowNullArgs (OpName) $left $right)
)
=>
(FoldNullBinary (OpName) $left $right)
```
