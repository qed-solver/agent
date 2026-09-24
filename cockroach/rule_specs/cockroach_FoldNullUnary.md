# Name: FoldNullUnary
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldNullUnary discards any unary operator with a null input, and replaces it
with a null value having the same type as the unary expression would have.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldNullUnary`, not the other rules in that file):

```
# FoldNullUnary discards any unary operator with a null input, and replaces it
# with a null value having the same type as the unary expression would have.
[FoldNullUnary, Normalize]
(Unary $input:(Null))
=>
(FoldNullUnary (OpName) $input)
```
