# OptimizeRepartition

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 11  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2045-2053
```

## Independent verifier review

**Verdict:** AGREE

The rule's only effect is flipping a physical planner hint — `optNumPartitions` on `RepartitionByExpression` from `None` to `Some(1)` — and its guard is purely syntactic (partition expressions non-empty and all foldable); a shuffle/repartition is a bag-identity over its child regardless of how many partitions it allocates, so before and after are bag-identical to the shared child and the only faithful encoding is the vacuous `before ≡ after`. This rests on a backend operator's bespoke physical semantics (partition count) that QED's bag-semantic model has no notion of, and the core language has no repartition operator at all — extending the DSL would only yield either a degenerate identity or two uninterpreted symbols that differ in an invisible field, so there is no non-trivial relational content to prove. ```
