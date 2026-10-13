# WindowReduceExpressions

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ReduceExpressionsRule.java

Note: ReduceExpressionsRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `WindowReduceExpressionsRule` variant (not CalcReduceExpressionsRule, FilterReduceExpressionsRule, JoinReduceExpressionsRule, ProjectReduceExpressionsRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

The rule's before pattern is a LogicalWindow, but the core language has no Window operator (RelRN exposes only scan/filter/project/join/set-ops/aggregate) and QED's JSONSerializer has no case for LogicalWindow — it falls through to "Not implemented" — so the pattern cannot even be encoded. Window partition/order/frame semantics have no bag-semantic meaning in the unchangeable prover, and the rewrite's per-partition constant elimination (dropping constant partition/order keys and reducing window aggregate operands) is exactly the per-partition aggregate reasoning QED cannot perform, since it models aggregates only up to bag equality of their input. Extending the DSL cannot close this gap because the trusted Rust prover itself lacks window semantics, so no JSON node could carry them. ```
