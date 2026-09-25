# MaterializedViewFilterScan

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 26  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MaterializedViewFilterScanRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rewrite's soundness rests on the materialization invariant that the MV table's stored bag equals the result of its defining query over the base table, but QED decides equivalence by universally quantifying over all instantiations of uninterpreted table symbols whose only per-table facts are key uniqueness and row-level "guaranteed" predicates, so a cross-table content equality cannot be stated as a premise. Any encoding using distinct symbols for the base table and the MV is therefore refuted by a countermodel (e.g. an empty or differently-populated MV), while identifying the two symbols collapses to a vacuous identity that proves nothing about the actual transformation. This is a limitation of the fixed prover's premise language — no DSL extension can close it, since JSONSerializer carries no inter-table constraint and the Rust prover itself cannot be modified. ```
