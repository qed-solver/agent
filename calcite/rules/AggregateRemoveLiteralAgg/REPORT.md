# AggregateRemoveLiteralAgg

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateRemoveLiteralAggRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests entirely on Calcite's internal semantics that `LITERAL_AGG(lit)` returns `lit` for every group, which is a specific algebraic property of one backend aggregate function; QED's model treats every aggregate call as an uninterpreted function of its group's input bag, so the before-side aggregate column is an unconstrained symbol that no amount of structural manipulation (group-key shape, arity, input, or a DSL builder addition) can pin to the constant the after-side project emits. Since the gap is in the prover's uninterpreted-aggregate semantics — not a missing operator or shape in the core language — no encoding, narrower or full, can state this rule without smuggling in exactly the identity QED cannot know, so UNSUPPORTED is the correct conclusion.
