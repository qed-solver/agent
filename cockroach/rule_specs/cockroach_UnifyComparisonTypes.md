# Name: UnifyComparisonTypes
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

UnifyComparisonTypes takes a mixed-type comparison between a non-constant and
a constant and, if appropriate, converts the constant to the type of the
non-constant to allow constraints to be generated.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `UnifyComparisonTypes`, not the other rules in that file):

```
# UnifyComparisonTypes takes a mixed-type comparison between a non-constant and
# a constant and, if appropriate, converts the constant to the type of the
# non-constant to allow constraints to be generated.
[UnifyComparisonTypes, Normalize]
(Comparison
    $left:(Variable)
    $right:(Const) &
        (Let ($result $ok):(UnifyComparison $left $right) $ok)
)
=>
((OpName) $left $result)
```
