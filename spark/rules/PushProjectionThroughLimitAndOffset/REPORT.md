# PushProjectionThroughLimitAndOffset

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 10  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PushProjectionThroughLimitAndOffset.scala, lines 27-43
```

## Independent verifier review

**Verdict:** AGREE

The rule's validity rests on Limit/Offset selecting a positional prefix of an ordered row stream while preserving row identity, but QED has no ordering semantics for Sort/Limit/Offset (qed.pdf §6.2) and models them as uninterpreted bag operators, under which SMT refutes Project(E, Limit(n, R)) ≡ Limit(n, Project(E, R)) (and the Offset/GlobalLimit variants) for any non-identity projection E — e.g., a Limit model that returns one fixed row on non-empty input breaks commutation with any E that doesn't fix that row. The only universally valid instance (identity projection) is vacuous, and adding a Limit/Offset builder via extend_dsl_file could only serialize the same uninterpreted sort QOp that the unmodifiable prover interprets opaquely, so no encoding can recover the required ordered-prefix/row-count axioms — consistent with the verified AGREE precedents for the structurally identical datafusion CommuteLimitProjection and same-backend spark EliminateLimits. ```
