# MultiplyByZero

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 41  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1170-1180
```

## Independent verifier review

**Verdict:** AGREE

MultiplyByZero is a pure scalar arithmetic identity — its soundness rests on the axiom ∀a. *(a, 0) = 0 (guarded by non-nullness of a), but QED's fixed SMT theory translates scalar operators to uninterpreted functions with no arithmetic axioms and no interpreted numeric zero constant (the only interpreted scalar operators are booleans, which is exactly why the DSL exposes And/Or/Not/True/False), so a countermodel with *(a, 0) ≠ 0 is always available and no encoding can avoid needing that axiom. This is the same verified limitation that forced the sibling constant-folding rules FoldPlusZero (a + 0 = a) and BitwiseAndByZero to be skipped, and an `extend_dsl_file` addition can't close it because new multiply/zero builders would merely serialize to additional uninterpreted symbols in the unmodifiable prover.
