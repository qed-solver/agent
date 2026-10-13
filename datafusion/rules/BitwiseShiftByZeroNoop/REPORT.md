# BitwiseShiftByZeroNoop

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1452-1457
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests on the algebraic identity x >> 0 = x, but in QED the shift operator can only appear as an uninterpreted scalar function, and the prover's fixed oracle theory (equality + UF, total order, addition, ite) contains no shift or zero-identity axiom, so no instance of an uninterpreted f satisfies f(x, 0) = x — indirect encodings (modeling the literal 0 as a constant/values table, key or "guaranteed" constraints on it) get at most congruence (f(x, c) = f(x, 0)), never the identity itself. This is a genuine limitation of the immutable Rust prover, not a missing DSL capability that extend_dsl_file could close: even a first-class shift builder would need the prover to know its axiom, which it doesn't. Any encoding that made the proof go through would have to replace the shift with an identity or ite-based surrogate, which would no longer be a faithful port of DataFusion's rule — exactly the "operator whose specific internal semantics QED cannot see through as an uninterpreted function" limitation in the reference.
