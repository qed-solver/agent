# CastLiteralFold

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 26  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 534-680
```

## Independent verifier review

**Verdict:** AGREE

QED models type casts as uninterpreted function symbols with no axioms — its only structural theorems are bag algebra, Boolean algebra, and NULL-lifting for primitive operators — so any faithful encoding of CAST(c)→c′ requires proving f(c)=c′ for all interpretations of f, which SMT correctly refutes with a countermodel. No special case rescues the rule: type erasure makes identity-cast cases inexpressible, the DSL exposes no NULL literal and QED has no NULL-propagation rule for uninterpreted symbols (so even CAST(NULL)→NULL is out), and the after-side folded value must be a concrete literal that can never be equationally linked to f(c). This is a genuine "bespoke internal semantics of an operator QED cannot see through as an uninterpreted function" limitation, not a missed encoding. ```
