# Name: FoldComparisonWithAny
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldComparisonWithAny evaluates a comparison operation over a constant
and an ANY/SOME clause, replacing the entire expression with a constant.
It iterates over elements in the clause and tries to find constants that
make the comparison a definite value.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldComparisonWithAny`, not the other rules in that file):

```
# FoldComparisonWithAny evaluates a comparison operation over a constant
# and an ANY/SOME clause, replacing the entire expression with a constant.
# It iterates over elements in the clause and tries to find constants that
# make the comparison a definite value.
[FoldComparisonWithAny, Normalize]
(AnyScalar
    $left:* & (IsConstValueOrGroupOfConstValues $left)
    $right:* & (IsTuple $right)
    $cmp:* &
        (Let
            ($result $ok):(FoldComparisonWithAny
                $cmp
                $left
                $right
            )
            $ok
        )
)
=>
$result
```
