# Name: FoldCast
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldCast is similar to FoldUnary, but it involves a cast operation. As with
FoldUnary, FoldCast applies as long as the evaluation would not cause an
error.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldCast`, not the other rules in that file):

```
# FoldCast is similar to FoldUnary, but it involves a cast operation. As with
# FoldUnary, FoldCast applies as long as the evaluation would not cause an
# error.
[FoldCast, Normalize]
(Cast
    $input:*
    $typ:* &
        (IsConstValueOrGroupOfConstValues $input) &
        (Let ($result $ok):(FoldCast $input $typ) $ok)
)
=>
$result
```
