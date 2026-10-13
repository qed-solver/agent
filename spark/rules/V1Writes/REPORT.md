# V1Writes

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 10  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/V1Writes.scala, lines 68-115
```

## Independent verifier review

**Verdict:** AGREE

V1Writes is a physical planning rule, not a semantics-preserving optimization — its defining steps (a deliberately non-identity Empty2Null coercion Project on partition columns, an ordering-dependent Sort insertion for requiredOrdering, and substitution of the child under the bespoke WriteFiles operator) mean the before/after plans are intentionally *not* equivalent relations, so there is no before≡after bag-equivalence statement for QED to make. QED models only bag semantics with no Sort/ordering interpretation (qed.pdf §6.2) and cannot see through a physical write operator's bespoke internal semantics, and even the most degenerate special case (no partition columns, empty requiredOrdering) collapses to an operator swap between two uninterpreted physical operators, which the prover cannot equate. The porter's UNSUPPORTED conclusion is correct. ```
