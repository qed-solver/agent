# AggregateProjectStarTable

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 11  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateProjectStarTableRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's `after` plan swaps the base-table scan for a scan of a *different* pre-aggregated materialized table chosen from the external planner's lattice, and only when such a materialization exists — so its validity rests on a cross-table catalog invariant (the materialized table's rows equal the base table's pre-aggregation at the given grain) plus conditional planner state, not on pure bag semantics of the shown operators. RuleScript can only introduce scans as independent uninterpreted relations and attach per-table first-order `guaranteed` check constraints, with no operator or constraint form that relates two distinct scans via an aggregate-equivalence, so QED treats the base and materialized scans as unrelated and would refute rather than prove the equivalence. This is a genuine QED/DSL limitation (bespoke materialization semantics + an unexpressible cross-table relational premise), not a symbol-sharing or encoding slip the porter failed to diagnose. ```
