# Name: FoldBinary
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldBinary evaluates a binary operation over constant inputs, replacing the
entire expression with a constant. The rule applies as long as the evaluation
would not cause an error. Any errors should be saved for execution time,
since it's possible that the given operation will not be executed. For
example:

SELECT CASE WHEN true THEN 42 ELSE 1/0 END

In this query, the ELSE clause is not executed, so the divide-by-zero error
should not be triggered.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldBinary`, not the other rules in that file):

```
# FoldBinary evaluates a binary operation over constant inputs, replacing the
# entire expression with a constant. The rule applies as long as the evaluation
# would not cause an error. Any errors should be saved for execution time,
# since it's possible that the given operation will not be executed. For
# example:
#
#   SELECT CASE WHEN true THEN 42 ELSE 1/0 END
#
# In this query, the ELSE clause is not executed, so the divide-by-zero error
# should not be triggered.
[FoldBinary, Normalize]
(Binary
    $left:* & (IsConstValueOrGroupOfConstValues $left)
    $right:* &
        (IsConstValueOrGroupOfConstValues $right) &
        (Let
            ($result $ok):(FoldBinary (OpName) $left $right) $ok
        )
)
=>
$result
```
