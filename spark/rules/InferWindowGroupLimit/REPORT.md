# InferWindowGroupLimit

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/InferWindowGroupLimit.scala, lines 54-138
```

## Independent verifier review

**Verdict:** AGREE

The rewrite's soundness rests on ordinal semantics: row_number/rank/dense_rank over an expanding (UNBOUNDED PRECEDING, CURRENT ROW) frame under a non-empty ORDER BY, plus the property that truncating each partition to its first k rows (WindowGroupLimit) leaves the rank values of all rows with rank ≤ k unchanged — and QED's decision procedure operates over bag semantics with uninterpreted functions, with no model of ordering, window frames, or rank (Sort/Limit/Window are explicitly excluded). Since the needed invariant is a statement about the internal behavior of those operators, encoding Window/WindowGroupLimit as uninterpreted symbols would leave before/after as unrelated opaque functions with no algebraic relationship for the prover to discharge, and extending the DSL can't close this either because the trusted, unchangeable Rust prover itself lacks list/ordering semantics. ```
