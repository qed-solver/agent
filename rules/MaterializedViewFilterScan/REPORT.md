# MaterializedViewFilterScan

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MaterializedViewFilterScanRule.java
```

## Independent verifier review

**Verdict:** AGREE

The porter's recorded failure was an LLM context-length error, not a real attempt — but the unsupported conclusion is independently correct. The rule's soundness rests on the externally maintained materialization invariant "the MV storage table's contents equal its defining query's result," i.e. a cross-table content equality between two independent uninterpreted tables; QED checks before/after equivalence under *all* instantiations of those tables, and the DSL's only per-table facts are keys (via `unique`) and row-level `guaranteed` predicates (not even exposed by any current `scan` builder), so no encoding can state that one table's bag equals a query over another. Hence every real encoding fails: distinct table names for the base table and the MV are refuted by a counterexample (e.g. an empty MV), while reusing one name degenerates to a vacuous identity — and besides the unstateable premise, the rule emits a catalog-dependent family of substitutions (via `SubstitutionVisitor` plus an internal Hep normalization program) rather than a fixed before/after plan pair.
