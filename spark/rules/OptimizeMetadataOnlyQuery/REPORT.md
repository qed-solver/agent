# OptimizeMetadataOnlyQuery

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 26  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/OptimizeMetadataOnlyQuery.scala, lines 46-182
```

## Independent verifier review

**Verdict:** AGREE

The rule's after-side is a constant `LocalRelation` whose rows are the partition specs pulled from the catalog/file-system, so its soundness rests on the physical invariant that a partitioned table's data, projected onto its partition columns (and filtered), yields exactly those catalog-determined partition rows, plus on Max/Min idempotence or aggregate distinctness. QED models scans and aggregates as uninterpreted bag symbols with no notion of physical partition layout, has no way to axiomatize a relation between two independent uninterpreted symbols (the scan and any metadata stand-in), knows no aggregate algebra beyond input bag-equality (so the `isAllDistinctAgg` side can't be justified either), and the DSL cannot even build a non-empty constant relation — so any encoding would be either inexpressible or refuted by SMT, making this a genuine QED modeling limitation rather than a missed encoding.
