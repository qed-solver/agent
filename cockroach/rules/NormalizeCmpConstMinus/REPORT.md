# NormalizeCmpConstMinus

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 44  **Verification rounds used:** 3

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

The rule's validity rests on ordered-arithmetic cancellation ((c1−a) cmp c2 ⟺ (c1−c2) cmp a) plus constant folding of c1−c2 to a concrete value, but QED decides equivalence over bag semantics with uninterpreted functions — Minus and the comparison operators are uninterpreted symbols with no additive/ordered algebra, and the DSL has no typed numeric constants to express c1, c2, or the folded result. SMT therefore admits interpretations (e.g. Minus as an arbitrary function) where the two filter predicates disagree, and no extend_dsl_file change can close this: a new builder would only emit another uninterpreted symbol, since interpreting arithmetic would require modifying the QED prover itself, which is off-limits. The independently verified AGREE-SKIPPED on the mirror rule NormalizeCmpMinusConst (21 attempts) corroborates that this is a fundamental prover limitation, not a symbol-sharing or encoding bug. ```
