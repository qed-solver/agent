# RemoveRedundantSorts

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 13  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/RemoveRedundantSorts.scala, lines 29-62
```

## Independent verifier review

**Verdict:** AGREE

Both branches of RemoveRedundantSorts rest on list-level properties that QED's bag semantics cannot express: the local branch is only sound when the child's guaranteed output ordering satisfies the requested one (`orderingSatisfies(child.outputOrdering, orders)`), and the global branch rewrites into `RepartitionByExpression`, a physical shuffle operator with no relational meaning. QED models relations as bags with no Sort operator, no ordering-guarantee symbol, and per-row table "guarantee" constraints cannot encode a row-order property, so the rule's actual correctness precondition is inexpressible rather than merely unbuilt. The only bag-checkable reading (Sort ≡ identity) is vacuously provable for any child but verifies none of the rule's soundness condition and would justify an incorrect rewrite of a top-level ORDER BY, confirming this is a fundamental prover limitation that extending RelRN cannot close. ```
