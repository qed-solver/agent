# CollapseGroupedSumOfCount

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 27  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/CollapseGroupedSumOfCount.scala, lines 35-124
```

## Independent verifier review

**Verdict:** AGREE

RelRN can express the two aggregate shapes, but QED serializes every aggregate call as an uninterpreted operator and only reasons about equality of its input bag, not about COUNT/SUM arithmetic. The Spark rewrite depends on the universal identity that summing one COUNT(1) per fine group over a coarser group equals summing/counting 1 over all raw rows in that coarse group. That identity is not a consequence of bag equality for arbitrary uninterpreted aggregate functions, so no faithful encoding is provable.
