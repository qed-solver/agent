# WindowReduceExpressions

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ReduceExpressionsRule.java

Note: ReduceExpressionsRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `WindowReduceExpressionsRule` variant (not CalcReduceExpressionsRule, FilterReduceExpressionsRule, JoinReduceExpressionsRule, ProjectReduceExpressionsRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

WindowReduceExpressions rewrites a Window's spec (constant-folding aggregation operands, dropping constant partition keys, dropping constant order keys), and its correctness rests on two things QED fundamentally lacks: window semantics (partition/order/frame-dependent values are not bag operations, and the DSL and serializer have no Window node to begin with — a listed QED limitation, not a closable DSL gap) and predicate-derived constant inference (QED cannot infer from an uninterpreted predicate that a filtered column takes a single value, and it knows no algebra for the concrete operators constant-folding relies on). Even the narrowest bag-encodable special case — a whole-partition window written as a group-by aggregate joined back to the input — still requires QED to prove a partition key is constant under a pulled-up filter, which is exactly the predicate entailment QED cannot do, leaving only a trivial no-op "rule" with identical before/after.
