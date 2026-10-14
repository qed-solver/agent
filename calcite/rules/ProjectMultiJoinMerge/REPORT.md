# ProjectMultiJoinMerge

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectMultiJoinMergeRule.java
```

## Independent verifier review

**Verdict:** AGREE

Calcite's `ProjectMultiJoinMerge` leaves the MultiJoin's inputs, join conditions, row type, and even the top-level project (reused verbatim in `onMatch`, which is only valid because the multi-join output is unchanged) all identical — it merely fills in the per-input `projFields` bitmaps that downstream physical rules consult for column pruning, so the transformation is bag-identical by construction. QED's decision procedure quantifies only over the bag semantics of relational expressions and has no concept of such non-semantic optimizer metadata; the DSL has no `MultiJoin`/`projFields` construct, and `extend_dsl_file` cannot close this gap because the unmodifiable prover's Q-expression language has no field for an annotation with no semantic effect, so any faithful encoding collapses to a vacuous before≡after identity. The only alternative — interpreting `projFields` as real per-input projections — would be a different (pushdown) rule whose validity rests on a "J' is J restricted to a subset of columns" correspondence between uninterpreted predicates and field indices that QED fundamentally cannot express or infer.
