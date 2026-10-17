# LimitZeroToEmpty

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 13  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/eliminate_limit.rs, lines 1-89
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire content is the Limit operator's own semantic axioms (fetch=0 yields the zero-row relation; fetch=None with skip=0 is the identity), but QED encodes Limit/Offset only as a free, uninterpreted QOp with no axioms about row counting or ordering, so any before/after pair with an uninterpreted QOp on only one side is unprovable for every instantiation — a fundamental limitation of the unmodifiable Rust prover, not a DSL gap (a Limit builder added via extend_dsl_file would only serialize to the same uninterpreted node). No genuine special case exists: pinning the input (e.g., to Empty or a scan) still leaves an unconstrained QOp term to equate with ∅/Q, and the only provable Limit-involving equivalences would be degenerate same-structure tautologies that are not this rewrite. ```
