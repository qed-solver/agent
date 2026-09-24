# Name: FoldNullBinaryLeft
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldNullBinaryLeft replaces the binary operator with null if its left input
is null and it does not allow null arguments.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldNullBinaryLeft`, not the other rules in that file):

```
# FoldNullBinaryLeft replaces the binary operator with null if its left input
# is null and it does not allow null arguments.
[FoldNullBinaryLeft, Normalize]
(Binary
    $left:(Null)
    $right:* & ^(AllowNullArgs (OpName) $left $right)
)
=>
(FoldNullBinary (OpName) $left $right)
```
