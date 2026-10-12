# ReplaceDeduplicateWithAggregate

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3343-3368
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness is a non-deterministic equivalence — both `Deduplicate(keys)` and `Aggregate(keys, First)` emit one row per key but each picks an arbitrary (implementation-defined) representative for the non-key columns — so it is equality up to representative choice, not the deterministic bag equality QED decides. QED models every aggregate as an opaque deterministic function of its input bag and knows nothing about its returned value, and `Deduplicate` is a Spark-internal operator whose bespoke pick-a-representative semantics QED cannot see through as an uninterpreted function, so the only core-language encodings of the before-side are either textually identical to the after-side (vacuous) or a differently-named picker the SMT solver refutes, with keys=all-columns collapsing to the already-proven DistinctToGroupBy.
