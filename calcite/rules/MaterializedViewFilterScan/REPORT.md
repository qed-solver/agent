# MaterializedViewFilterScan

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 58  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MaterializedViewFilterScanRule.java
```

## Independent verifier review

**Verdict:** AGREE

MaterializedViewFilterScan's soundness rests on the cross-table invariant that the MV scan's bag equals its defining query over the base table (e.g. bag(MV) = Filter(D, bag(base))), which QED's theory cannot state: every scan is an independent uninterpreted bag whose only declared properties are per-table primary keys and check predicates (the JSON format's per-table "key"/"guaranteed" fields — the full schema vocabulary JSONSerializer emits), so no relation between two distinct table symbols is expressible and `Filter(Q∧D, base) ≡ Filter(Q, MV)` is unprovable for arbitrary independent bags. The only provable single-table encoding (filter-merge: `Filter(Q∧D, base) ≡ Filter(Q, Filter(D, base))`) eliminates the MV table entirely and is therefore a different rule, not a special case of this one; the gap is a fundamental limitation of the prover's per-table theory, not a missing DSL builder, so no `extend_dsl_file` change (which is gated on the existing prover/JSON semantics) could close it. ```
