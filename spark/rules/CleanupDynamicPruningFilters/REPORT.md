# CleanupDynamicPruningFilters

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 49  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/dynamicpruning/CleanupDynamicPruningFilters.scala, lines 35-94
```

## Independent verifier review

**Verdict:** AGREE

The rule's only plan-level move is deleting a DynamicPruning conjunct (semantically `key IN <dynamically computed value set>`) on the grounds that a same-row conjunct `key = const` already pins the key, but the value set is a backend-internal artifact with no plan representation, so soundness rests on the entailment `key = const ⟹ key IN <dpp-values>` between independent uninterpreted symbols plus Spark's DPP invariant that values come from the join's filtering side. QED's bag-semantic model has no way to relate an uninterpreted DPP predicate to an equality predicate or to a dynamically-materialized set, and no DSL operator can manufacture that link — the porter's complete_fragment=true negative confirms this is a fundamental limitation, not an encoding bug. ```
