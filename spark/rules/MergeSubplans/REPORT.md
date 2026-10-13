# MergeSubplans

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 49  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/planmerging/MergeSubplans.scala, lines 151-341
```

## Independent verifier review

**Verdict:** AGREE

The rule's essence is hoisting duplicated one-row subplans into a shared CTE (WithCTE/CTERelationDef/CTERelationRef, evaluate-once), but I verified JSONSerializer's Rel switch only supports scan/values/filter/project/join/correlate/group/union/intersect/except/sort with default->throw, so no let/CTE node kind exists in the fixed fragment and no Java-side extension can serialize into one. Additionally, the rewrite is bag-semantically transparent (shared reuse is only physically observable), and the reference path GetStructField(ScalarSubquery(CTERef), i) over CreateNamedStruct would require a get∘pack identity that is unprovable for uninterpreted functions — so any encoding is either refuted or collapses to the vacuous before ≡ after identity, consistent with the prior InlineCTE (inverse rule) exhaustion. ```
