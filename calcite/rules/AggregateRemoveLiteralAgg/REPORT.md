# AggregateRemoveLiteralAgg

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateRemoveLiteralAggRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests entirely on the algebraic identity LITERAL_AGG(c) ≡ c — i.e., the call must evaluate to the constant operand for every group (including the whole-bag group in the no-key case where the rule substitutes COUNT to keep a valid aggregate) — but QED axiomatizes every aggregate call as an uninterpreted bag-functional whose only provable property is that identical calls over bag-equal inputs agree. To the SMT decision procedure, the before-side LITERAL_AGG output column and the after-side projected constant are unrelated terms, so a counter-instantiation (LIT(G) ≠ c for some group) is always admissible, and no RuleScript encoding — constant-valued input column, symbol sharing, empty operand list — can teach the fixed prover a specific aggregate's constant-returning value, which makes this a genuine QED limitation (an aggregate algebraic identity it cannot know) rather than a missing DSL capability. ```
