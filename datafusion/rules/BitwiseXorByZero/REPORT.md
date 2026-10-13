# BitwiseXorByZero

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1376-1381
```

## Independent verifier review

**Verdict:** AGREE

The rule requires the bitwise-Xor identity `x ^ 0 = x`, but RuleScript can only introduce `^` as an uninterpreted projection/predicate and QED has no prover-level axioms relating that uninterpreted operator to a zero constant. Adding a Java DSL builder could expose the shape, but it cannot add the missing operator theory, so the equivalence is not valid under arbitrary SMT interpretations.
