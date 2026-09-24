# Name: NormalizeCmpConstMinus
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

NormalizeCmpConstMinus builds up constant expression trees on one side of the
comparison, in cases like this:
cmp          cmp
/  \         /  \
[-]   2  ->  [-]   a
/   \        /   \
1     a      1     2

See NormalizeCmpPlusConst for more details.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `NormalizeCmpConstMinus`, not the other rules in that file):

```
# NormalizeCmpConstMinus builds up constant expression trees on one side of the
# comparison, in cases like this:
#      cmp          cmp
#      /  \         /  \
#    [-]   2  ->  [-]   a
#   /   \        /   \
#  1     a      1     2
#
# See NormalizeCmpPlusConst for more details.
[NormalizeCmpConstMinus, Normalize]
(Eq | Ge | Gt | Le | Lt
    (Minus $leftLeft:(Const) $leftRight:^(ConstValue))
    $right:(Const) &
        (ArithmeticErrorsOnOverflow
            (TypeOf $leftLeft)
            (TypeOf $right)
        ) &
        (CanConstructBinary Minus $leftLeft $right) &
        (Let
            ($result $ok):(FoldBinary Minus $leftLeft $right) $ok
        )
)
=>
((OpName) $result $leftRight)
```
