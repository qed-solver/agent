# CleanUpTempCTEInfo

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PushdownPredicatesAndPruneColumnsForCTEDef.scala, lines 276-282
```

## Independent verifier review

**Verdict:** AGREE

The only difference between before and after is the value of `originalPlanWithPredicates` on `CTERelationDef`, a pass-through node whose bag is exactly its child's, and that field is planner bookkeeping (idempotency for CTE pushdown) that no rule or the executor reads for query results. QED's model sees only bag relations with uninterpreted row-level functions, so both patterns necessarily compile to the identical Q-expression over the child, and any `extend_dsl_file` operator carrying the field would either hard-code its inertness into `semantics()` (making the "proof" circular) or attach a JSON attribute the immutable Rust prover does not interpret. The rule's correctness rests entirely on the Spark-planner-level fact that the field is semantically inert, which is outside the bag-semantic model by construction, so the porter's UNSUPPORTED conclusion is sound.
