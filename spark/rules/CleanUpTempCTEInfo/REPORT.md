# CleanUpTempCTEInfo

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 17  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PushdownPredicatesAndPruneColumnsForCTEDef.scala, lines 276-282
```

## Independent verifier review

**Verdict:** AGREE

CleanUpTempCTEInfo only clears the plan-metadata field CTERelationDef.originalPlanWithPredicates (Some→None) and changes none of the rows the plan produces, so under QED's bag-semantic model the before/after plans are the same relation and the metadata difference is neither representable nor observable (and the DSL has no CTE operator to distinguish the two node states). The only faithful encoding is therefore a vacuous before()==after() identity — precisely the degenerate proof the reviewer rejected — leaving no non-trivial semantic equivalence for QED to establish, so the UNSUPPORTED conclusion is correct. ```
