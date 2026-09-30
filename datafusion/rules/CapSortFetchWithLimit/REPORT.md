# CapSortFetchWithLimit

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_limit.rs, lines 161-185
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire semantic content — capping a Sort's fetch to min(F, skip+fetch) and rewriting `Limit(0, fetch, Sort(F))` to a fetch-capped `Sort` — is defined purely in terms of row position in sort order (top-N), which QED's bag-semantic model has no notion of (Sort/Limit carry no bag semantics and act only as uninterpreted operators). Both sides share the identical input relation and differ only in the structure and fetch arguments of those uninterpreted wrappers, so no non-vacuous bag-identity core exists and a DSL Sort builder (even though the JSON serializer could already carry a sort node) would not help, since the immutable prover has no ordering semantics to check the fetch manipulation against.
