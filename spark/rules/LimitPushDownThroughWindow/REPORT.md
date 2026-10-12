# LimitPushDownThroughWindow

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 11  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/LimitPushDownThroughWindow.scala, lines 32-59
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests on order-dependent facts that no bag-semantic model can express: `Limit(n, Sort(orderSpec))` must retain the *first* n rows of the global order (bag semantics cannot distinguish that from an arbitrary n-element subset), and RowNumber/Rank/DenseRank over a global `UNBOUNDED PRECEDING → CURRENT ROW` frame must assign to those leading rows values that depend only on preceding rows (prefix-safety), which is a specific window-frame algebra QED cannot know about a function it can at best treat as uninterpreted. QED's JSON format can technically carry a `sort` node, so adding Sort/Limit/Window builders via `extend_dsl_file` wouldn't help — a sort would reduce to a bag identity (making any "proof" vacuous and semantically empty) or an unconstrained bag transform (making the two sides unprovable), and a standalone Limit has no expressible node at all. This matches the reference limitation list exactly (Sort/Limit/Offset/Window/Sample have no bag-semantic meaning; bespoke operator internals can't be reasoned about), and the porter's cited prior SKIPPED verdict on CockroachDB's `PushLimitIntoWindow` for the same order-dependence is consistent with my independent analysis — the gap is in the prover, not a surface DSL mistake. ```
