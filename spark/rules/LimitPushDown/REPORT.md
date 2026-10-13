# LimitPushDown

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 13  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 926-1030
```

## Independent verifier review

**Verdict:** AGREE

Every case of this rule rests on Spark's non-bag limit semantics (Limit(n, X) denotes an arbitrary subset of ≤ n rows, possibly in order), which QED explicitly does not model — Limit/Offset have no bag-semantic meaning and at best surface as uninterpreted operators with no axioms, so SMT has nothing to relate two distinct limit expressions. Worse, most cases are simply not bag equalities under any deterministic semantics: e.g. `Limit(n, A ⋈ B) → Limit(n, Limit(n, A) ⋈ Limit(n, B))` changes row counts, and `Limit(1, GroupBy(X)) → Limit(1, Project(keys, Limit(1, X)))` loses de-duplication and can select different group rows, so no correct bag-equivalence encoding exists. Adding a Limit builder (the JSON serializer can already carry sort/limit fields) would only expose the axiom-free uninterpreted QOp, confirming this is a fundamental prover limitation, not a missed encoding. ```
