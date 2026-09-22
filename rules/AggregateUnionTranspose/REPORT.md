# AggregateUnionTranspose

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateUnionTransposeRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests on the split/merge algebra of specific aggregate functions (SUM additivity, MIN idempotence, COUNT→SUM0): it claims that re-aggregating the per-branch aggregate *results* equals aggregating the raw unioned rows. QED models every aggregate call as an uninterpreted symbol with no algebraic knowledge, and the top aggregate's input bag (the per-branch f-images) is not the same bag the original aggregate sees (the raw rows), so the two applications of the same uninterpreted function have universally different inputs and the SMT solver can build a countermodel for every candidate encoding; this limitation lives in the trusted prover's semantics, not in a missing DSL builder, so no RuleScript encoding of the transpose itself (not even a narrowed special case) is provable. ```
