# SortMerge

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortMergeRule.java
```

## Independent verifier review

**Verdict:** AGREE

SortMerge's entire correctness claim is limit/sort composition — LIMIT_T over (SORT_B, LIMIT_B over X) rewriting to (SORT_B, LIMIT min(T,B) over X) — which requires modeling ordering and top-N row selection, semantics QED explicitly does not have (Sort/Limit/Offset/Window/Sample have no bag-semantic meaning). The core DSL exposes no sort/limit operator at all (RelRN has only scan/filter/project/join/set-ops/aggregate), so the pattern cannot even be written faithfully; adding one would not help, since QED would reduce both sides to the same underlying bag and the literal fetches plus the min(T,B) computation (done at rule-fire time in Java, not in the plan) would remain unexpressible and unprovable. (Note: the porter's logged "reason" was an LLM context-length API error, but its transcript shows it had already converged on exactly this limitation before failing, so the UNSUPPORTED conclusion is substantively correct.) ```
