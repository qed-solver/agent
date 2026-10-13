# ExtractExternalUDFFromWindow

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 11  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/externalUDF/ExtractExternalUDFFromWindow.scala, lines 33-71
```

## Independent verifier review

**Verdict:** AGREE

The rule rewrites a `Window` node into a `Project` over a differently-defined `Window`, and its correctness hinges on the Window operator evaluating each window expression to the same per-row value under a fixed partition/order/frame, so that an external UDF applied above the window sees the identical input. QED reasons purely under bag semantics and has no model for list/ordering operators — `Window` (like `Sort`/`Limit`/`Sample`) carries no bag meaning and has no Q-expression/JSON form the unmodifiable Rust prover could interpret — and the two distinct Window invocations (one computing `externalUDF(w)`, the other computing bare `w`) cannot be related by any uninterpreted-symbol trick, so no DSL extension or narrower special case can render this provable. ```
