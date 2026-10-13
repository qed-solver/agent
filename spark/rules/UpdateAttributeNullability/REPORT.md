# UpdateAttributeNullability

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 27  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/analysis/UpdateAttributeNullability.scala, lines 34-56
```

## Independent verifier review

**Verdict:** AGREE

UpdateAttributeNullability is a static type-metadata (nullable-flag) propagation pass with no effect on bag-level query results — it only rewrites attribute annotations, and QED's Q-expression semantics equip every type with a distinguished NULL value without ever consulting the schema's nullable flag, so any before/after pair differing only in that flag encodes as the identical Q-expression and "proves" vacuously. The rule's sole content lives in nullability metadata, and the hard boundary forbids modifying the QED prover to make it consult nullable flags (extending the Java DSL alone cannot create any provable, non-vacuous difference), so it is genuinely outside RuleScript/QED's expressible-and-checkable scope. ```
