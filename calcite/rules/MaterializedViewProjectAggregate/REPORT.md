# MaterializedViewProjectAggregate

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewProjectAggregateRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule replaces Project(Aggregate(Scan T)) with a scan of the MV's physical table, and its soundness rests on an external database invariant — the registered MV table is maintained to equal the result of its defining aggregate — rather than on relational semantics. Under QED's model the MV table and the source table are independent uninterpreted relations, so universal quantification over arbitrary instantiations actively refutes Scan(MV) = Aggregate(Scan(T)); no encoding can bridge this (identifying the two tables would make the rule a vacuous identity, and table "guaranteed" constraints are per-row predicates, not inter-table equalities), and the rollup path independently requires re-aggregation algebra across granularities (e.g., sum-of-sums over a coarser grouping) that QED explicitly does not model. ```
