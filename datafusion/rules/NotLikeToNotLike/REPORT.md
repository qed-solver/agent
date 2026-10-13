# NotLikeToNotLike

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/utils.rs, lines 360-367
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire content is the definitional duality ¬(x LIKE y) ≡ x NOT LIKE y, and QED's fixed logic has no axiom or definitional expansion for LIKE/NOT_LIKE — so a faithful encoding, where NOT_LIKE is a distinct uninterpreted predicate, leaves `not(like(...))` and `not_like(...)` as independent symbols the prover can never relate (its stated limitation on predicate inference between independent symbols). The only alternative is to encode NOT_LIKE as `Not` of the same LIKE symbol, which collapses before/after into the identical plan and reduces any "proof" to a vacuous identity that assumes the theorem; no DSL extension can bridge this, since the unchanging prover sees uninterpreted function symbols either way.
