# ProjectMultiJoinMerge

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 9  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectMultiJoinMergeRule.java
```

## Independent verifier review

**Verdict:** AGREE

The porter's analysis is correct: `RelOptUtil.projectMultiJoin` rebuilds the MultiJoin with every field identical except `projFields` (a `List<@Nullable ImmutableBitSet>`), which is pure planner metadata consumed later by decomposition heuristics (`LoptOptimizeJoinRule`) with zero effect on the node's rows or row type, so `Project(P, M) ⟹ Project(P, M')` is a metadata annotation, not a logical rewrite. RuleScript's core language has no notion of such per-operator planner metadata — not even an n-ary join with annotation hooks — so any faithful encoding makes `before()` and `after()` literally the same RelRN tree, a vacuous identity whose "proof" says nothing about the annotation's semantic inertness, and any non-trivial variant (e.g. actually pruning input columns) would assert a different rule than the one being ported. ```
