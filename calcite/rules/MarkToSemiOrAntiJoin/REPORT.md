# MarkToSemiOrAntiJoin

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 65  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MarkToSemiOrAntiJoinRule.java
```

## Independent verifier review

**Verdict:** AGREE

The before() pattern must name an actual LEFT_MARK join, but its marker column — a per-left-row existential summary (∃ right row satisfying the condition, at left-row bag multiplicity) appended as an extra output column — is bespoke operator semantics that no operator in RuleScript's core language can name: the JAR's JoinRelType has no LEFT_MARK (so RelRN/JSONSerializer cannot produce a kind the prover accepts), and the prover's fixed kind vocabulary has no mark-join/EXISTS operator, which extend_dsl_file cannot add since it can only touch the Java DSL layer. Reconstructing the marker from primitives would require an IS NOT NULL/EXISTS predicate the DSL doesn't expose or aggregate-algebra reasoning (count>0 ↔ ∃) that QED is explicitly stated not to have, and in any case the resulting expression would no longer be a pattern that matches a real backend mark-join plan, so a faithful port is genuinely out of reach.
