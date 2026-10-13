# OptimizeCsvJsonExprs

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 30  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/OptimizeCsvJsonExprs.scala, lines 44-484
```

## Independent verifier review

**Verdict:** AGREE

Each sub-rewrite asserts a parser-specific identity, e.g. `JsonToStructs(StructsToJson(x)) = x`, `π_i(JsonToStructs(x, fullSchema)) = JsonToStructs(x, prunedSchema)`, or `GetStructField(MultiGetJsonObject(x, paths), i) = GetJsonObject(x, path_i)`. QED treats scalar/parser operators as uninterpreted with opaque outputs and has no axioms for Spark's JSON/CSV parser algebra, so no RuleScript encoding can make those equivalences universally provable.
