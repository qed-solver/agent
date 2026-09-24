# Name: NormalizeCmpMinusConst
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

NormalizeCmpMinusConst builds up constant expression trees on one side of the
comparison, in cases like this:
cmp         cmp
/  \        /  \
[-]   2  ->  a  [+]
/   \           /   \
a     1         2     1

See NormalizeCmpPlusConst for more details.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `NormalizeCmpMinusConst`, not the other rules in that file):

```
# NormalizeCmpMinusConst builds up constant expression trees on one side of the
# comparison, in cases like this:
#      cmp         cmp
#      /  \        /  \
#    [-]   2  ->  a  [+]
#   /   \           /   \
#  a     1         2     1
#
# See NormalizeCmpPlusConst for more details.
[NormalizeCmpMinusConst, Normalize]
(Eq | Ge | Gt | Le | Lt
    (Minus $leftLeft:^(ConstValue) $leftRight:(Const))
    $right:(Const) &
        (ArithmeticErrorsOnOverflow
            (TypeOf $right)
            (TypeOf $leftRight)
        ) &
        (CanConstructBinary Plus $right $leftRight) &
        (Let
            ($result $ok):(FoldBinary Plus $right $leftRight) $ok
        )
)
=>
((OpName) $leftLeft $result)
```
