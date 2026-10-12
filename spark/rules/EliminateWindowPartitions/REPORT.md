# EliminateWindowPartitions

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/EliminateWindowPartitions.scala, lines 28-41
```

## Independent verifier review

**Verdict:** AGREE

QED has no Window operator and models aggregation opaquely per key tuple, so it cannot relate the per-partition groupings a spec induces with vs. without a foldable (constant) column; the whole correctness of the rule is exactly "partitioning by (spec, const) == partitioning by spec," which requires constant/FD reasoning QED lacks, and the strongest self-referential probe (group-by (k,k) vs (k)) is rejected before SMT because the two sides carry different key-tuple arities and QED cannot equate window-function values computed under two different key tuples — consistent with the independently-verified SKIPPED sibling ReduceWindowPartitionCols.
