# Name: NormalizeCmpPlusConst
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

NormalizeCmpPlusConst builds up constant expression trees on one side of the
comparison, in cases like this:
cmp          cmp
/   \        /   \
[+]    2  ->  a   [-]
/   \             /   \
a     1           2     1

The rule can only perform this transformation if all of the following criteria
are met:

1. The generated Minus expression will error if there is an overflow (see
ArithmeticErrorsOnOverflow).
2. A Minus overload for the given input types exists and has an appropriate
volatility.
2. There is no error when evaluating the new binary expression.

NOTE: Ne is not part of the operator choices because it wasn't handled in
normalize.go either. We can add once we've proved it's OK to do so.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `NormalizeCmpPlusConst`, not the other rules in that file):

```
# NormalizeCmpPlusConst builds up constant expression trees on one side of the
# comparison, in cases like this:
#       cmp          cmp
#      /   \        /   \
#    [+]    2  ->  a   [-]
#   /   \             /   \
#  a     1           2     1
#
# The rule can only perform this transformation if all of the following criteria
# are met:
#
#   1. The generated Minus expression will error if there is an overflow (see
#      ArithmeticErrorsOnOverflow).
#   2. A Minus overload for the given input types exists and has an appropriate
#      volatility.
#  2. There is no error when evaluating the new binary expression.
#
# NOTE: Ne is not part of the operator choices because it wasn't handled in
#       normalize.go either. We can add once we've proved it's OK to do so.
[NormalizeCmpPlusConst, Normalize]
(Eq | Ge | Gt | Le | Lt
    (Plus $leftLeft:^(ConstValue) $leftRight:(Const))
    $right:(Const) &
        (ArithmeticErrorsOnOverflow
            (TypeOf $right)
            (TypeOf $leftRight)
        ) &
        (CanConstructBinary Minus $right $leftRight) &
        (Let
            ($result $ok):(FoldBinary Minus $right $leftRight)
            $ok
        )
)
=>
((OpName) $leftLeft $result)
```
