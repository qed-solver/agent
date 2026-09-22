# ProjectJoinJoinRemove

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 60  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectJoinJoinRemoveRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule is valid only because the bottom join's condition is an equi-join on Y's unique key, guaranteeing at most one Y row per X row — but QED models join conditions as uninterpreted predicates and cannot infer that selectivity from the unique-key constraint (predicate inference/entailment it explicitly cannot do). A constant-true join predicate is a counterexample that doubles the output multiplicity, so no correct encoding (even one reusing the same predicate symbol and building the top-join condition over only the non-Y columns) is provable; this is a genuine QED limitation, not a missing DSL shape the porter failed to find. ```
