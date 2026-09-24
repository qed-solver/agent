# MaterializedViewProjectAggregate

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewProjectAggregateRule.java
```

## Independent verifier review

**Verdict:** AGREE

This rule is a materialized-view rewrite, not a local bag-equivalence transformation: it must replace a `Project(Aggregate)` query with a separate view scan, possibly with compensation and a roll-up aggregate. QED can only relate expressions built over the same uninterpreted scans/operators, and it treats aggregate functions as uninterpreted except for input-bag equality, so it cannot assume a view scan equals its defining aggregate or prove re-aggregation identities. Any faithful encoding would therefore require unsupported premises about the view and aggregate algebra, making the UNSUPPORTED conclusion correct.
