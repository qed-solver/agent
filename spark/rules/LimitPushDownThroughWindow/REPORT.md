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

The rewrite `Limit(k, Window(R))` ⇒ `Window(Limit(k, Sort(R)))` is sound only by a positional invariant — with empty partition specs and `(UNBOUNDED PRECEDING, CURRENT ROW)` frames, the row_number/rank/dense_rank values of the first k rows in `orderSpec` order depend only on those k rows, so truncating before the window preserves exactly the surviving rows' rank labels. QED models bag semantics only and explicitly lacks list/ordering semantics: `Sort`, `Limit`, and `Window` carry no bag-semantic meaning in Q-expressions and would be, at best, uninterpreted operators with no axioms (qed.pdf §6.2), so the needed identity is a non-universal commutation claim between uninterpreted functions that an SMT solver refutes under some instantiation, and extending the DSL with Sort/Limit/Window builders could not help because the fixed prover has no theory of row position or window frames to interpret them. There is genuinely no bag-semantic fragment of this rule left for the prover to discharge, and the physical guards (`maxRows`, `topKSortFallbackThreshold`) are heuristics rather than logic, so the UNSUPPORTED conclusion is correct.
