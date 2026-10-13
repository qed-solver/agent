# NormalizeCmpMinusConst

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

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
```

## Independent verifier review

**Verdict:** AGREE

The rule's validity rests on the ordered-arithmetic identity (a − c₁) cmp c₂ ⟺ a cmp (c1 + c2), which QED cannot derive because it models Plus/Minus and the comparison operators as uninterpreted symbols with no additive algebra, no typed integer constants, and no way to fold c1 + c2 into a concrete value — an SMT countermodel (arbitrary interpretations of the uninterpreted functions/predicate) breaks the equivalence for any encoding. Closing the gap would require the prover itself to interpret these operators arithmetically, which is off-limits (the QED prover is unchangeable), and the side guards (overflow policy, overload/volatility checks, foldability) are equally backend-specific and unmodelable. ```
