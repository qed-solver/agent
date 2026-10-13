# PushFilterIntoSort

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 7  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_filter.rs, lines 885-889
```

## Independent verifier review

**Verdict:** AGREE

I confirmed against the ground-truth RelRN.java that no Sort builder or record exists (JSONSerializer can carry a sort node, but the trusted, unmodifiable prover decides equivalence only under bag semantics, under which Sort has no list/ordering meaning, per QED's stated limitations). The rule's sole non-trivial content — that the survivors of Filter(p, Sort(R)) retain the same relative sorted order as Sort(Filter(p, R)) — is a sequence property that cannot be expressed in the bag algebra (row-wise uninterpreted predicates/projections cannot capture relative ordering across a whole relation). Any encodable reduction would degenerate to the tautology Filter(p, R) ≡ Filter(p, R), certifying nothing about order preservation, so the porter's UNSUPPORTED conclusion is correct. ```
