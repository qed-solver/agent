# Name: FoldUnary
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldUnary is similar to FoldBinary, but it involves a unary operation over a
single constant input. As with FoldBinary, FoldUnary applies as long as the
evaluation would not cause an error.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldUnary`, not the other rules in that file):

```
# FoldUnary is similar to FoldBinary, but it involves a unary operation over a
# single constant input. As with FoldBinary, FoldUnary applies as long as the
# evaluation would not cause an error.
[FoldUnary, Normalize]
(Unary
    $input:* &
        (IsConstValueOrGroupOfConstValues $input) &
        (Let ($result $ok):(FoldUnary (OpName) $input) $ok)
)
=>
$result
```
