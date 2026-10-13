# V2ScanRelationPushDown

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 61  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/v2/V2ScanRelationPushDown.scala, lines 48-1382
```

## Independent verifier review

**Verdict:** AGREE

I confirmed against the full DSL source that a pushed-down scan can only be encoded as a fresh uninterpreted scan symbol, which QED must equate with `Filter(preds, base)` under *every* instantiation — impossible, since the DSL doesn't even expose per-table `guaranteed` constraints and such constraints could at best express one-way inclusion, never two-way equality with a second table. The limit/offset and sample sub-rules rest on ordering/limit semantics QED explicitly doesn't model, and the partial-aggregate-pushdown fragments (min-of-min, sum-of-sum, count→sum, avg→sum/count) require aggregate algebra QED cannot know — so no faithful, non-trivial provable fragment of this rule exists, and the porter's diagnosis is sound rather than a modeling miss.
