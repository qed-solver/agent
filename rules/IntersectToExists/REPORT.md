# IntersectToExists

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/IntersectToExistsRule.java
```

## Independent verifier review

**Verdict:** AGREE

The porter's stated reason was merely an LLM context-length crash, but the conclusion is correct for a real reason: the EXISTS arm of the rewrite depends on per-field IS NOT DISTINCT FROM comparisons (and correlated-subquery semantics), which QED treats as uninterpreted symbols with no built-in equality meaning, while the before side is a bare INTERSECT containing no such symbols — so QED would have to prove equivalence for *every* instantiation of that comparison (e.g. one that is always false, making the result just A rather than A∩B) and fail. The DSL also has no correlate/exists builder at all (only JSONSerializer's output format can carry them), but extending the DSL would not close the gap, since the blocker is QED's inability to see through the comparison operator's row-identity semantics, not a missing builder. ```
