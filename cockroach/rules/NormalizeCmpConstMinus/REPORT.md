# NormalizeCmpConstMinus

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 43  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

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
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests on the interpreted arithmetic/order identity c1 − a OP c2 ⟺ (c1 − c2) OP a (plus the backend's overflow/fold side conditions), i.e. on the specific algebraic semantics of subtraction and of Eq/Ge/Gt/Le/Lt. QED models minus and comparison as uninterpreted projection/predicate symbols with no ring or order axioms, and that prover is the fixed, unmodifiable arbiter — so under its universal quantification over all instantiations the before and after filters are not equivalent, and no DSL-level encoding or extension can supply the missing axioms. This is a genuine fundamental limitation of QED (it "can't reason about a backend operator's bespoke internal semantics"), not a missed modeling trick. ```
