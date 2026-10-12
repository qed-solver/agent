# OptimizeCsvJsonExprs

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 10  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/OptimizeCsvJsonExprs.scala, lines 44-484
```

## Independent verifier review

**Verdict:** AGREE

Every sub-rewrite in this bundle depends on a specific algebraic identity between Spark's bespoke JSON/CSV operators — JsonToStructs(StructsToJson(x)) ≡ x (inverse composition), field_i(JTS(full schema)) ≡ field_0(JTS(pruned schema)) (parse-consistency under schema projection), field_i(MultiGetJsonObject) ≡ GetJsonObject (a Hive-compatible first-match semantics), and struct(field_0..n(JTS)) ≡ JTS (reconstruction). QED models all scalar expressions as uninterpreted functions with no axioms relating them, so each of these identities is refutable by a countermodel in the QED semiring semantics (e.g., two independent function symbols f, g for which g∘f ≠ id), and no faithful relational encoding — however cleverly symbol-shared — can make any of them universally valid. This is precisely the "operator whose specific internal semantics QED cannot see through as an uninterpreted function" limitation, and the guards in the original rule (empty options, corrupt-record exclusion, same-input coalesce checks) encode exactly the parser-internal conditions QED cannot express, so UNSUPPORTED is correct. ```
