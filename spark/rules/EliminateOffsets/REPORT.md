# EliminateOffsets

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 12  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3134-3144
```

## Independent verifier review

**Verdict:** AGREE

All three case laws require the prover to know Offset's positional "skip the first k rows" semantics — an ordered-list property that QED's bag model explicitly lacks (Sort/Limit/Offset have no bag-semantic meaning and would be uninterpreted), and case 2 additionally needs static cardinality (maxRows) bounding over the child, which QED does not reason about. No core-language operator can express "drop the first k rows," so even the most degenerate special case (e.g., Offset over an empty relation) still hinges on the unmodeled Offset semantics, and adding a DSL builder would confer no decidability since the fixed prover has no bag interpretation for it — matching qed.pdf §6.2's stated limitation. ```
