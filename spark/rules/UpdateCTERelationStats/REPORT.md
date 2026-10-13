# UpdateCTERelationStats

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 8  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 495-526
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire content is plan-node metadata bookkeeping — it threads a `statsMap` through the tree and calls `withNewStats` on `CTERelationDef`/`CTERelationRef` nodes, changing only the cached `stats: Option[Statistics]` field while leaving the emitted rows (and the plan structure) completely unchanged. That state has no home in either layer: RuleScript's core language has no CTE/def-ref operator and no per-node attributes or side channels, and QED's JSON model carries only table schemas (fields/types/keys/constraints) plus bag-semantic relational algebra, so `statistics` simply cannot be represented. Any faithful encoding would therefore degenerate to the vacuous identity `before() == after()` — trivially "provable" but proving nothing about the rule's actual behavior (the id-keyed stats lookup and attachment), which is a genuine modeling limitation rather than a missed encoding trick. ```
