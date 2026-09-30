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

The rule `A | 0 -> A` depends on the algebraic identity that bitwise-or with the numeric zero literal is the identity operation, but QED's SMT theory models all non-boolean scalar operators as fully uninterpreted functions and provides no numeric literals or arithmetic axioms, so there is no way to express or prove this identity. This is a fundamental prover-side limitation rather than a missing DSL capability: even if `extend_dsl_file` exposed a bitwise-or operator symbol, the unmodifiable prover would still treat it as uninterpreted and lack the `bitwise_or(x, 0) = x` axiom, which is the same reason the analogous `FoldPlusZero`/`FoldMinusZero`/`FoldDivOne` numeric-folding precedents are correctly rejected. ```
