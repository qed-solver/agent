# AggregateProjectStarTable

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 6  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateProjectStarTableRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness depends on two things QED structurally cannot capture: (1) its "after" side is a scan of a *different* materialized table chosen at match time from the planner's external lattice state, and the only constraint that would make that substitution valid — "materialization = group-by/aggregate over the star table" — is an inter-table definitional relation, while QED's per-table "guaranteed" constraints (per JSONSerializer) are row-wise predicates over a single table's own columns; even the no-rollup exact-match special case degenerates into swapping one uninterpreted scan for an unrelated uninterpreted scan, which no constraint in the format can link. (2) The roll-up variant (e.g. rolling COUNT(*) up as SUM over a coarser group set) requires additivity/regrouping algebra for specific aggregate functions, which QED explicitly does not model for uninterpreted aggregate operators — and this is a prover-level limitation no DSL extension can fix, so the porter's UNSUPPORTED conclusion is correct. ```
