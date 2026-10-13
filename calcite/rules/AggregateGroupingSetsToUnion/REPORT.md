# AggregateGroupingSetsToUnion

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 26  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateGroupingSetsToUnionRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's only match is a non-SIMPLE aggregate (real GROUPING SETS/CUBE/ROLLUP), but RuleScript's `Aggregate` and QED's serialized `group` node carry only a single flat grouping-key function, so the "before" operator is unnameable in the pattern language and `extend_dsl_file` can't close the gap because the fixed Rust prover has no multi-set-aggregate semantics to consult (a new JSON field would be ignored or rejected, yielding a vacuous proof against a simple aggregate). Compounding this, the rule folds `GROUPING` aggregate calls into literal bit-mask constants, which requires knowledge of that specific aggregate's algebra that QED explicitly lacks (aggregates are uninterpreted beyond bag equality of their input), and no meaningful narrower case exists: the rule bails on single-set/SIMPLE aggregates, and any encodable stand-in for the before side is either the trivial identity or circular (defining it in terms of the after side's own semantics).
