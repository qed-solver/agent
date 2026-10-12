# EliminateLimits

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 7  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3098-3124
```

## Independent verifier review

**Verdict:** AGREE

All three families of EliminateLimits depend on semantics QED explicitly lacks: the elimination case needs a `maxRows` cardinality upper bound (QED models only key uniqueness, never row counts/statistics), and both `Limit(0, X) ≡ ∅` and the nested-merge `Limit(le, Limit(ne, X)) ≡ Limit(min(le,ne), X)` require axioms that Limit selects a first-n prefix of an ordered row stream (qed.pdf §6.2 states QED has no ordering semantics for Limit/Offset/Order By — the emitted `sort` QOp is a fully uninterpreted bag operator, so SMT refutes even the degenerate `Limit(n, Limit(n, X)) ≡ Limit(n, X)`). The gap is in the trusted, unmodifiable prover's semantics, not the DSL surface: a new Limit/Sort builder could only serialize that same uninterpreted operator and could not supply the missing axioms, so no RuleScript encoding can be provable.
