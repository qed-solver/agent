# MultiplyByZero

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1170-1180
```

## Independent verifier review

**Verdict:** AGREE

The rule `A * 0 → 0` (and `0 * A → 0`) rests on the multiplicative-zero axiom of actual arithmetic multiplication, but in RuleScript `*` can only be introduced as an uninterpreted scalar operator and QED's fixed theory (equality, uninterpreted functions, total order, natural-number addition, ite) has no multiplication or any axiom about such a symbol, so the universal claim `∀A. f(A,0) = 0` is refutable by a countermodel (e.g., an interpretation with f(x,0)=x). A DSL extension can add a builder emitting that operator name but cannot add the required axiom to the immutable Rust prover, and no narrower relational special case (unique keys, fixed shapes) escapes the unprovable identity, so this is genuinely outside what RuleScript+QED can prove — consistent with the independently AGREE-skipped MultiplyByOne precedent. ```
