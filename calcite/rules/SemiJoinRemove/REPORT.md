# SemiJoinRemove

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 8  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinRemoveRule.java
```

## Independent verifier review

**Verdict:** AGREE

`X SEMI JOIN Y ON c → X` is not a bag-semantic equivalence, and QED decides it over *every* instantiation of the uninterpreted symbols, so any countermodel — Y empty, or even a single X-row with no match under c (including with a true literal condition) — makes the universal proof impossible. The rule's soundness rests on Calcite's plan-global "advisory semi-join" precondition, i.e. the cross-table entailment that the semi-join's existence filter is the identity, which is precisely the predicate-inference/entailment between independent uninterpreted symbols QED cannot reason about, and the DSL/QED offer no side-condition or premise channel (table "guaranteed" constraints are row-level properties like uniqueness, which cannot express non-emptiness or full match coverage). So this is a genuine QED limitation, not an encoding mistake or a missing builder — no narrower non-degenerate special case is provable either. ```
