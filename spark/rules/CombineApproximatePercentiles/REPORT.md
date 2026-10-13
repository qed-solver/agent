# CombineApproximatePercentiles

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/CombineApproximatePercentiles.scala, lines 73-225
```

## Independent verifier review

**Verdict:** AGREE

The rule is sound only because Spark’s fused ApproximatePercentile has a backend-specific decomposition property: element i of the fused multi-percentage result equals the standalone call for percentage i, combined with array-indexing semantics. QED treats aggregates and scalar expressions as uninterpreted symbols with only bag-input equality and has no list/array element semantics, so it cannot prove or even meaningfully encode that identity without assuming the conclusion.
