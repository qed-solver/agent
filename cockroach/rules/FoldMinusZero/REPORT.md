# FoldMinusZero

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/numeric.opt

FoldMinusZero folds $left - 0 for numeric types. This rule requires a check
that $left is numeric because JSON - INT is valid and is not a no-op with a
zero value.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldMinusZero`, not the other rules in that file):

```
# FoldMinusZero folds $left - 0 for numeric types. This rule requires a check
# that $left is numeric because JSON - INT is valid and is not a no-op with a
# zero value.
[FoldMinusZero, Normalize]
(Minus $left:(IsAdditiveType (TypeOf $left)) $right:(Const 0))
=>
(Cast $left (BinaryType Minus $left $right))
```
```

## Independent verifier review

**Verdict:** AGREE

FoldMinusZero is a purely scalar arithmetic normalization (`x - 0` → `cast(x, τ)`) whose validity rests on the subtract-by-zero identity and on type-cast semantics; the core language has no scalar minus or cast operator at all (RelRN.Minus is relational EXCEPT), and any operator introduced — including via a DSL extension — serializes as an uninterpreted function symbol with no axioms in the unmodifiable Rust prover, so `minus(x, 0)` and `cast(x)` — or even `minus(x, 0)` and bare `x` — are unequal in some model of QED's fixed theory (equality, UF, order, nat addition, ite) for every instantiation, making UNSUPPORTED the correct call.
