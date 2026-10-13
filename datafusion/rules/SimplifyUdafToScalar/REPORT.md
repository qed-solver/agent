# SimplifyUdafToScalar

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 29  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1677-1684
```

## Independent verifier review

**Verdict:** AGREE

The assigned source (L1677–1685) is a pure dispatch that hands the aggregate to an arbitrary per-UDAF, user-supplied `simplify()` closure, so there is no fixed after-pattern and thus no fixed before/after relational pair for QED to be asked to prove — this is a meta-hook, not a concrete transformation. Even specializing to a real UDAF rewrite (e.g. `percentile_cont(col, 0.0) → min(col)`) would still fail, because QED models aggregate functions as uninterpreted and knows nothing about their algebra beyond bag-equality of the input; the simplification changes the aggregate operator itself, which the prover cannot see through as an uninterpreted function. ```
