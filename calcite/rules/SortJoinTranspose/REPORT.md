# SortJoinTranspose

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortJoinTransposeRule.java
```

## Independent verifier review

**Verdict:** AGREE

SortJoinTranspose's soundness rests entirely on ordering semantics — pushing a Sort's top-(offset+fetch) down through an outer join so the surviving rows still cover the outer Sort/Offset/Fetch slice — and QED explicitly does not model list/ordering semantics (Sort/Limit/Offset have no bag-semantic meaning), and the core DSL exposes no Sort builder at all. Under pure bag semantics the two sides are not even equivalent, because the inner fetch drops rows out of the join input (e.g. a LEFT join loses unmatched-key rows from the other side entirely), so no bag-equivalence proof can exist for the real rule. The only bag-valid variant — a plain sort with no fetch/offset that drops nothing — reduces both sides to the same join bag and would be a vacuous tautology, not a genuine port of the rule. ```
