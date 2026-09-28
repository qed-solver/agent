# NormalizeCmpPlusConst

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 41  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

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
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests on the arithmetic identity (a + b) ⋖ c ⟺ a ⋖ (c − b), which ties together the Plus, Minus, and comparison symbols — but in QED's SMT encoding these are independent uninterpreted symbols with no axioms relating them, so the equivalence is not a logical validity (e.g., a countermodel with plus/minus mapped arbitrarily over a small domain refutes it), and the prover is exactly the kind of system that "can't reason about entailment between independent symbols or an operator's bespoke internal semantics." Additionally, the DSL itself (RexRN) provides no numeric literal constructor — only boolean True/False — so the rule's defining features, the ConstValue operands b, c and the folded constant result = c − b, cannot even be expressed; any encoding degenerates to claiming equality between structurally different applications of independent symbols, for which no full or non-trivial special case (including modeling b, c as scan columns) is provable. ```
