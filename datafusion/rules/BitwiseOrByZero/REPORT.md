# BitwiseOrByZero

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1303-1308
```

## Independent verifier review

**Verdict:** AGREE

The identity A | 0 = A holds only by integer bitwise-OR algebra, but in RuleScript the operator can only be introduced as a generic (uninterpreted) projection symbol serialized by name, and QED's SMT theory — equality over uninterpreted functions, a total order, and three-valued And/Or — has no integer/bit-vector axioms, so a countermodel with or(x, c) ≠ x is always available. The one encoding QED could prove would be three-valued logical `A OR FALSE = A`, which is a different rule and would misrepresent DataFusion's integer BitwiseOr. This matches the stated limitation of operator-specific internal semantics QED cannot see through, and `extend_dsl_file` cannot help since the missing piece is axioms inside the unmodifiable prover theory. ```
