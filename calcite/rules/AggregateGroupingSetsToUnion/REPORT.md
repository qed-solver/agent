# AggregateGroupingSetsToUnion

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateGroupingSetsToUnionRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's before-side is an Aggregate over GROUPING SETS (with GROUPING()/GROUPING_ID handling), which RuleScript cannot name: `RelRN.Aggregate` accepts only a flat group-key list, and the ground-truth `JSONSerializer`'s "group" node carries only flat `keys` + `function` (`getGroupSets()`/`getGroupingCalls()` are dropped), so the only component that could supply grouping-sets semantics — the unmodifiable Rust prover — has no model of the very operator the rule rewrites. No non-trivial special case exists, since every firing instance has a grouping-sets aggregate on the before side, and encoding that side as its own union-of-per-set-aggregates would make `before()` structurally identical to `after()`, a tautology rather than a port of the rule. This is a genuine fixed-prover limitation (operator semantics absent from QED's JSON/semiring model, in the same class as Sort/Window), not a missing Java builder that `extend_dsl_file` could close.
