# AggregateFilterToFilteredAggregate

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 7  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateFilterToFilteredAggregateRule.java
```

## Independent verifier review

**Verdict:** AGREE

The after side is a SQL filtered aggregate, and QED's model has no notion of a FILTER attribute on an aggregate call — the aggregate-call JSON schema is exactly {operator, operand, distinct, ignoreNulls, type}, and the unmodifiable prover equates aggregate calls only by identical operator plus bag-equal input, giving it no means to relate "uninterpreted f over the P-filtered bag" to "f-with-filter over the full bag" (different input bags). Every faithful alternative encoding fails: a fresh uninterpreted operator for the filtered call is refutable by SMT, a plain aggregate over the full bag is not equivalent and not provable, and the only provable form is the structurally identical pre-filtered aggregate, which is the identity, not this rule — a genuine limitation in QED's aggregate semantics that no Java-side DSL/serializer extension can close, since the Rust prover lacks the filter semantics regardless of what JSON we emit. ```
