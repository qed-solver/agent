# EliminateLimits

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3098-3124
```

## Independent verifier review

**Verdict:** AGREE

QED deliberately gives Sort/Limit/Offset no bag-semantic meaning — per qed.pdf, Limit is only expressible as the uninterpreted QOp(Limit, n, Q) with no axioms — so `Limit(0,Q)=∅` and `Limit(a,Limit(b,Q))=Limit(min(a,b),Q)` are not derivable for an uninterpreted function, and the maxRows≤n elimination case additionally needs a cardinality side condition that RRule's bare before/after interface cannot express and which makes the unguarded universal statement false. This is corroborated by the DSL ground truth: `RelRN.java` exposes no Limit/Sort builder at all, and `extend_dsl_file` could at best surface QED's uninterpreted QOp, which the prover's bag-based theory (its explicit "List semantic" limitation) cannot reason about — a genuine QED limitation, not a missing or mismodeled encoding. ```
