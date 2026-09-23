# ProjectMultiJoinMerge

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectMultiJoinMergeRule.java
```

## Independent verifier review

**Verdict:** AGREE

Calcite's `ProjectMultiJoinMerge` only enriches a `MultiJoin` with `projFields` planner metadata while leaving the logical relation unchanged, and RuleScript/QED has no `MultiJoin` operator or metadata side channel in its bag-semantic model. A faithful encoding would either collapse to a trivial identity or require QED to reason about Calcite-internal `MultiJoin` semantics it does not model, so no nontrivial proof of this rewrite is possible.
