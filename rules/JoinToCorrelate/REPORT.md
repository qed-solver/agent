# JoinToCorrelate

**Status:** FAILED
**Source backend:** Apache Calcite
**Porter attempts used:** 150  **Verification rounds used:** 5

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/JoinToCorrelateRule.java
```

## Independent verifier review

**Verdict:** DISAGREE

The porter never actually attempted the rule — it died on an LLM context-length HTTP 400 mid-exploration (its last note was literally "add the Correlate record to RelRN.java and test it"), so the UNSUPPORTED claim is an infrastructure failure, not a technical finding. The only gap is a missing DSL builder, not a QED limitation: JSONSerializer.java already emits JSON for `LogicalCorrelate` (kind/left/right) and for `RexFieldAccess` on correlation variables via the `Env.recorded`/`lifted` binding machinery, so the prover side is ready — extend RelRN.java with a `Correlate` record that creates a `CorrelationId`, builds the right side as `right.filter(cond)` where the join predicate's left-side references become `RexBuilder.makeFieldAccess(corrVar, i)` (a new correlated-field RexRN), and calls `RelBuilder.correlate(id, ty, rightRel)`. Then encode before() = `L.join(ty, P, R)` and after() = that correlate, reusing the same uninterpreted predicate symbol P applied to (correlated L fields, R fields): at minimum INNER is plain bag equality over uninterpreted symbols, and the rule's `!generatesNullsOnLeft()` guard (admitting INNER/LEFT/SEMI/ANTI/SINGLE) should be coverable as FULL scope, falling back to a PARTIAL tag only if a specific join kind resists the prover. ```
