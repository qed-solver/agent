# EliminateSerialization

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 10  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/objects.scala, lines 40-72
```

## Independent verifier review

**Verdict:** AGREE

Every branch of EliminateSerialization is sound only because a deserializer is the inverse of its matching serializer (deser(ser(x)) = x) on matching types, and the AppendColumns branch additionally depends on the internal behavior of the backend's AppendColumns/AppendColumnsWithObject operator pair. QED models projections as uninterpreted functions with no equation/entailment reasoning between them, so a faithful encoding is unprovable (as the porter's tested encoding confirmed), and any provable variant must either make before/after structurally identical (round-trip erased to a pass-through) or axiomatize the inverse law — i.e. assume the rewrite's own content, leaving only a vacuous tautology. No DSL operator or scan constraint can supply a specific backend operator's semantics, so this is a genuine QED limitation (no function entailment / bespoke operator semantics), not a naming or modeling bug the porter missed. ```
