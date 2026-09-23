# MaterializedViewOnlyAggregate

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewOnlyAggregateRule.java
```

## Independent verifier review

**Verdict:** AGREE

The porter's stated reason is just a pipeline crash (context-length HTTP 400), not an analysis, but the UNSUPPORTED conclusion is nonetheless correct on the merits. The rule's soundness rests on aggregate-algebra identities QED by design cannot know: rollup/roll-down re-partitioning (e.g., SUM over a coarser grouping equals the SUM of SUMs over a finer grouping, COUNT(*) rolled up via SUM, idempotent MIN/MAX), and its union-rewriting path additionally needs complementary-filter derivation between independent predicate symbols. QED only equates aggregates that are structurally identical with bag-equal inputs and has no way to constrain an MV's materialized table to equal the result of the MV's defining aggregate over the query's base scan, so the only provable encoding would be the degenerate no-op where the MV aggregate is structurally identical to the query's — not a genuine instance of the rule.
