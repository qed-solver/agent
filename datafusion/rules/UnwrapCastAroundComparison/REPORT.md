# UnwrapCastAroundComparison

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 10  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/unwrap_cast.rs, lines 1-250
```

## Independent verifier review

**Verdict:** AGREE

The rewrite `cast(x) cmp y ⟺ x cmp cast(y)` is only sound because of the specific cast's value-algebra (injectivity/monotonicity, literal in-range) plus static type-level guards; in QED every operator — the cast and each comparison — is an uninterpreted function with no axioms, and all VarTypes are erased to INTEGER in the prover, so that commutation is genuinely not a universal identity (the porter's complete, non-timed-out SMT countermodel is the expected answer, not an encoding bug). No DSL extension could help: the prover has no cast theory to hook into, and the pattern language offers no way to state the required side conditions (range/losslessness), so even the degenerate sub-case `cast(lit) = lit` would be unprovable since the "literal" is just an arbitrary column value. ```
