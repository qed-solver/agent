# AggregateFilterToFilteredAggregate

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateFilterToFilteredAggregateRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's validity rests entirely on the algebraic identity `agg(input restricted to rows where P) ≡ agg FILTER (WHERE P)(full input)` — the internal semantics of a *filtered* aggregate call — and QED equates aggregate calls only by same uninterpreted operator plus bag-equality of input, knowing nothing else about aggregate algebra, so it cannot relate the two sides. The "after" side cannot even be faithfully encoded: QED's JSON model of an aggregate (`group.function`) carries only `operator/operand/distinct/ignoreNulls/type` with no `filter` field, and the DSL's `AggCall` has no filter parameter. Extending the Java-side DSL cannot close this gap because the missing semantics lives in the (off-limits) Rust prover, so the only encodable version would be a degenerate identity — UNSUPPORTED is genuinely correct (the porter's recorded HTTP-400 error is a mechanical failure, but its pre-failure analysis had already landed on this exact limitation).
