# NormalizeCmpMinusConst

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2

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

The rewrite's validity rests on the value-level algebraic identity (x − c1) OP c2 ⟺ x OP (c2 + c1), but in QED the Minus, Plus, and comparison operators are all uninterpreted function/predicate symbols with no axioms, and the prover's JSON format offers no way to declare group/inverse/order properties over function symbols, so SMT cannot derive the required predicate entailment for any of Eq/Ge/Gt/Le/Lt. No relational embedding (e.g., the two filters over a shared scan) or specialization to particular constants can escape this, since even degenerate instances need facts like minus(x,0)=x, and the FoldBinary/ArithmeticErrorsOnOverflow guards are backend value-domain semantics (overflow behavior) that QED's abstract uninterpreted model does not represent at all — this is a fundamental QED limitation, not a missing DSL builder.
