# CorrelateUncollectOuter

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/CorrelateUncollectOuterRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's validity rests on `Uncollect`'s (Unnest's) bespoke internal semantics — specifically that `isOuter=true` guarantees at least one output row (a NULL row) even when the expanded collection is empty — which is what lets the LEFT-Correlate's NULL-padding case be fully absorbed so that LEFT collapses to INNER; QED treats operators as uninterpreted bag functions with no concept of collection expansion or output-cardinality guarantees, and cannot see that `Uncollect(isOuter=true)` is non-empty (nor accept a side-premise that the correlate's right side is guaranteed non-empty), so the equivalence is not provable. Moreover, `Uncollect` has neither a `RelRN`/`RexRN` builder nor any case in the QED JSON/Q-expr serialization format (only Correlate and correlated field-access are partially supported), so the rule is not even expressible. This is a fundamental QED limitation (unseeable operator-internal semantics), not a missing encoding the porter could have fixed.
