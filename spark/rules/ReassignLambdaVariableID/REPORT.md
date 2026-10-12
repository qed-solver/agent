# ReassignLambdaVariableID

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 42  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/objects.scala, lines 251-294
```

## Independent verifier review

**Verdict:** AGREE

This rule is a purely syntactic alpha-conversion of `LambdaVariable` bound variables, whose validity rests on binder scoping and equivalence-under-renaming — notions QED's first-order bag-semantics translation (uninterpreted functions over relations) has no concept of, and neither the DSL nor the prover's JSON format exposes any lambda/binder scalar node to represent a `LambdaVariable` at all. Since the Rust prover is the unchanging arbiter, this cannot be closed by a `extend_dsl_file` change: a binder node emitted by the serializer would be uninterpretable to the prover anyway. Hence any non-vacuous encoding must name the old-ID and new-ID variants as distinct uninterpreted symbols, which QED treats as independent and cannot equate, while the only provable encoding — identical before/after patterns — is the vacuous identity P = P that encodes nothing of the actual rewrite. ```
