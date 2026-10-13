# PushProjectionThroughLimitAndOffset

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 7  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PushProjectionThroughLimitAndOffset.scala, lines 27-43
```

## Independent verifier review

**Verdict:** AGREE

The validity of all three cases (Project pushed under LocalLimit, under GlobalLimit∘LocalLimit, and under Offset) rests on Limit/Offset having positional "keep/skip the first k rows" semantics, but QED is a bag-semantic prover with no axioms for Sort/Limit/Offset — they surface only as uninterpreted, axiom-free QOps, so it cannot relate Limit(n, Project(p, X)) to Project(p, Limit(n, X)), nor can it derive even degenerate facts like Limit(n, ∅) = ∅ or Offset(k, ∅) = ∅ that a minimal special case would still require. The RelRN API exposes no Limit/Sort builder, and adding one (the JSON serializer can carry sort/limit fields) would merely surface the uninterpreted operator without confering any decidability, since the fixed prover's bag theory has no interpretation for it — a genuine QED limitation, not a missing DSL capability, consistent with the independently verified skips of the sibling Spark rules EliminateLimits, EliminateOffsets, and LimitPushDown. ```
