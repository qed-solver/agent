# SortUnionTranspose

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortUnionTransposeRule.java
```

## Independent verifier review

**Verdict:** AGREE

SortUnionTranspose's correctness rests entirely on top-N ordering semantics — pushing a Sort(offset O, fetch F) into each UNION ALL branch as a Sort(fetch O+F) and re-sorting only holds because of the sorted top-(O+F) argument. QED explicitly does not model list/ordering semantics (Sort/Limit/Offset have no bag-semantic meaning), and the RuleScript core language exposes no Sort/Limit/Offset operator at all, so the rule cannot even be expressed, let alone genuinely verified; extending the DSL with a Sort builder would only make both sides collapse to the same union (a vacuous proof), not an actual check of the top-N push-down. ```
