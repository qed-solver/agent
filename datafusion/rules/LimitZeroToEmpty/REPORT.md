# LimitZeroToEmpty

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 8  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/eliminate_limit.rs, lines 1-89
```

## Independent verifier review

**Verdict:** AGREE

The rule's core claim is a cardinality axiom about LIMIT — a literal fetch of 0 (or the skip-0/no-fetch identity branch) constrains the number of output rows — and QED's bag-semantic model has no Limit/Offset operator and no row-count or ordering axioms (Limit is listed alongside Sort/Window/Sample as having no bag-semantic meaning), so a `LIMIT 0` node would be indistinguishable from an arbitrary uninterpreted relation and cannot be proved equal to Empty for any input. The DSL exposes no Limit builder and `JSONSerializer` does not even have a `LogicalLimit` case, so extending the DSL could at best get the pattern to serialize — it cannot supply the missing axioms to the immutable Rust prover, unlike Sort, whose bag-identity effect the prover can absorb. Even the narrowest special case (e.g., limiting an already-empty input) requires the same unmodeled limit semantics, so no non-trivial provable special case exists.
