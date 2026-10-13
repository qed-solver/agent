# CalcSplit

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 27  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/CalcSplitRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's before side is a single fused Calc node, and the QED prover's JSON/Q-expression contract — with the unchangeable Rust translator as the final arbiter — has a closed set of relational nodes (scan, values, filter, project, join, correlate, union/intersect/except, group, sort) with no fused-Calc node and no uninterpreted relation-valued operator slot; a fused Calc is by definition project∘filter over bag semantics, so any faithful encoding of the before side yields a tree structurally identical to the after side, making the only provable version a vacuous identity. extend_dsl_file cannot close the gap: emitting a new "calc" node kind would be rejected by the unchangeable Rust prover, and modeling Calc as project∘filter in the Java layer is by construction the after pattern itself, so there is no non-vacuous encoding to attempt. ```
