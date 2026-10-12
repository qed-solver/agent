# CheckCartesianProducts

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 10  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3160-3183
```

## Independent verifier review

**Verdict:** AGREE

`CheckCartesianProducts` is a pure validation rule — its only effect is to `throw` a compilation error on a detected Cartesian join, so it produces no rewritten "after" plan, while RRule/QED can only model and prove bag-semantic equivalence between two concrete relations (there is no rejection/exception construct anywhere in the DSL, serializer, or prover contract). On top of that, its core decision (`isCartesianProduct`) is a *syntactic, plan-structural* inspection of which columns the join condition references — a meta-level property of the operator that QED's uninterpreted, bag-semantic semantics has no way to see or reason about (and no clean relational equivalence to stand in for it, since a one-side-only condition does not make the join bag-equal to a pure cross product). So there is genuinely no faithful before/after pair to encode, and the porter's UNSUPPORTED conclusion stands.
