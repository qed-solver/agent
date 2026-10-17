# NotLikeToNotLike

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 64  **Verification rounds used:** 6

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/utils.rs, lines 360-367
```

## Independent verifier review

**Verdict:** AGREE

DataFusion's `simplify_not_like` is a pure scalar definitional identity — `NOT(e LIKE p) ≡ e NOT_LIKE p` — whose entire soundness rests on the backend axiom `NOT_LIKE ≡ NOT(LIKE)`. QED has no semantics for `LIKE`/`NOT_LIKE` (both are uninterpreted operators, and QED "can't reason about predicate inference/entailment between independent symbols or a backend operator's bespoke internal semantics"), so either the prover can't relate the two independent symbols (unprovable), or the pipeline's lowering of `NOT_LIKE` to `NOT(LIKE(...))` collapses both sides to the identical expression, a vacuous X≡X tautology that proves nothing about the rule. There is no non-trivial sub-case to fall back to, since the identity *is* the rule's content, and no `extend_dsl_file` change to `RelRN`/`RexRN`/`JSONSerializer` can supply the operator algebra — that lives in the frozen Rust prover.
