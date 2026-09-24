# Name: FoldComparison
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldComparison is similar to FoldBinary, but it involves a comparison
operation. As with FoldBinary, FoldComparison applies as long as the
evaluation would not cause an error.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldComparison`, not the other rules in that file):

```
# FoldComparison is similar to FoldBinary, but it involves a comparison
# operation. As with FoldBinary, FoldComparison applies as long as the
# evaluation would not cause an error.
[FoldComparison, Normalize]
(Comparison
    $left:* & (IsConstValueOrGroupOfConstValues $left)
    $right:* &
        (IsConstValueOrGroupOfConstValues $right) &
        (Let
            ($result $ok):(FoldComparison (OpName) $left $right)
            $ok
        )
)
=>
$result
```
