# RemoveNoopLimit

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 17  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/eliminate_limit.rs, lines 1-89
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests on Limit/offset semantics — that `LIMIT 0` truncates to the empty relation and that `Limit{skip=0, fetch=None}` is a no-op — which QED fundamentally cannot model (Sort/Limit/Offset/Window/Sample have no bag-semantic meaning, so any Limit node is a free uninterpreted operator with no row-count or order axioms). This is a genuine QED limitation rather than a missing DSL builder, since adding a Limit constructor would still leave the prover with no semantics to reason about, and the only "proof" of the no-op branch via a no-op Sort proxy restates QED's sort-identity axiom instead of establishing Limit removability.
