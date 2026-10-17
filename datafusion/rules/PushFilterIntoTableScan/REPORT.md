# PushFilterIntoTableScan

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 27  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_filter.rs, lines 1224-1301
```

## Independent verifier review

**Verdict:** AGREE

The rewrite Filter(P, Scan(T)) ⇒ Scan(T, filters=[P]) is sound only under the DataFusion TableProviderFilterPushDown::Exact contract — the pushed predicate's effect is a storage-engine internal semantic, not a relational-algebraic fact. QED models scans as uninterpreted bags with only per-row key/check constraints (no predicate parameter on the scan, no inter-table definitional relation), so the "after" side has no encodable form: a fresh table symbol would be an independent bag (the equivalence is refutable, not merely unproven), and the only bag-equal expression in the core language is Filter(P, Scan(T)) itself, which is a tautology rather than the rule. This falls squarely under the documented limitation that QED cannot model a backend operator's bespoke internal semantics, so the UNSUPPORTED conclusion is correct. ```
