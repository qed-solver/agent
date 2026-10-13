# SimplifyConditionals

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 72  **Verification rounds used:** 5

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 703-782
```

## Independent verifier review

**Verdict:** AGREE

SimplifyConditionals' actual content rests on three-valued conditional semantics — the `cond.nullable` guard, the `EqualNullSafe(cond, TRUE)` (IS TRUE) arm, and null-literal And/Or behavior — none of which QED's model can express, since predicates are total two-valued booleans with no nullability, no IS-NOT-DISTINCT-FROM, and no null propagation, a limitation of the fixed Rust prover that no DSL extension can change. The only encodable subcases (ite beta-reductions like If(TRUE, v, w) → v) are tautologies of the model's own definitional IF, confirmed by the porter's 0ns empirical proofs, so they verify nothing about the rule's null-handling content. The UNSUPPORTED conclusion is a genuine semantic limitation, not a missed encoding.
