# SortJoinTranspose

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortJoinTransposeRule.java
```

## Independent verifier review

**Verdict:** AGREE

SortJoinTranspose is meaningful only through ordering semantics: the outer Sort/Offset/Fetch must be preserved while a derived Fetch/Limit is pushed into one join input. QED explicitly models queries with bag semantics and has no Sort/Limit/Offset/ordering semantics, so any encoding that removes those operators degenerates to the vacuous identity of the same join.
