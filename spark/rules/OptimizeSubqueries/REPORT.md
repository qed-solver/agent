# OptimizeSubqueries

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 57  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 389-490
```

## Independent verifier review

**Verdict:** AGREE

The rule's only relational-algebraic transformation is removing a top-level Sort from a subquery body (Sort(R) ⇒ R), and QED's bag-semantic model has no Sort operator at all — Sort/Limit/Window/Sample require list semantics and are explicitly outside its decidable fragment, which the porter confirmed empirically: after properly extending RelRN.java with a Sort builder (now present in the DSL source) and serializing a clean `sort` node, the minimal Sort(scan) ⇒ scan test returned provable=false with no panic, proving the wall is in the prover, not the DSL. Every encoding of this rule's actual rewrite must place a Sort in the before-pattern, so no narrower sort-based instance can be provable. The rule's remaining branches — re-running the entire optimizer on the subquery, preserving the top Aggregate through re-optimization, and Spark exprId/attribute-identity bookkeeping in the COUNT-bug path — are backend-internal meta-operations (no SubqueryExpression operator exists in the DSL, and "optimizer.execute(...)" is a fixed-point operation with no before/after RA pattern), so they are inexpressible rather than merely untried; a fresh attempt cannot succeed. ```
