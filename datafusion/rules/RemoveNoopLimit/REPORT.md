# RemoveNoopLimit

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/eliminate_limit.rs, lines 1-89
```

## Independent verifier review

**Verdict:** AGREE

Both halves of this rule (fetch=Literal(0) ⇒ empty relation; fetch=None with skip=Literal(0) ⇒ drop the Limit as identity) draw their entire correctness from Limit/Offset operator semantics, which QED does not model — the prover treats limit/sort as uninterpreted operations with no cardinality or ordering axioms (qed.pdf §6.2), so the SMT solver can refute both equivalences against any input relation. This is a limitation of the trusted prover, not a DSL surface gap: even if `extend_dsl_file` added a Limit/Sort builder to `RelRN` (which currently exposes none), no axioms like `limit(0,R)=∅` or `offset(0,R)=R` could be introduced, since the Rust prover is unreachable and must stay as-is. This matches the independently-reviewed `LimitZeroToEmpty` precedent from the same source, which was likewise found unprovable. ```
