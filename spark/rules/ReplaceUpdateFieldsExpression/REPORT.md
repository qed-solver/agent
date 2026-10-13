# ReplaceUpdateFieldsExpression

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/UpdateFields.scala, lines 82-87
```

## Independent verifier review

**Verdict:** AGREE

The rule inlines the definitional expansion of an uninterpreted scalar operator (UpdateFields(S, ops) → If(IsNull(S), NULL, CreateNamedStruct(GetStructField(S,i), …))), so its correctness rests entirely on the backend-internal semantics of `UpdateFields` — precisely the "operator whose specific internal semantics QED cannot see through as an uninterpreted function" limitation. QED treats `UpdateFields`, `If`, `IsNull`, `CreateNamedStruct`, and `GetStructField` as distinct uninterpreted function symbols with no axioms tying `UpdateFields` to that expansion, so the two sides fail under arbitrary instantiation and no non-vacuous (nor any narrower) encoding is provable. This is a real QED-prover gap, not a missing DSL builder: `extend_dsl_file` can only add Java records, not the definitional axioms the unmodifiable prover would need.
