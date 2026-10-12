# EliminateOffsets

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3134-3144
```

## Independent verifier review

**Verdict:** AGREE

All three subcases of EliminateOffsets are axioms about row-ordering semantics — skipping the first n rows in order, producing an empty relation when the offset reaches the child's maxRows, and additivity of nested offsets — and QED is a bag-semantics decision procedure with no model of ordering at all (Sort/Limit/Offset/Window/Sample have no bag-semantic meaning, and Offset is an uninterpreted/no-semantics operator in the prover), so none of the rewrites is entailed for all instantiations. A DSL extension could at most add a Java-side Offset builder, but the missing Offset axioms would have to live in the QED prover itself, which is the fixed, unmodifiable arbiter, so the rule is genuinely outside RuleScript/QED. ```
