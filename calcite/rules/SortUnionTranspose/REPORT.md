# SortUnionTranspose

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 5  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortUnionTransposeRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule’s correctness depends on row-order/top-N semantics: each union branch is capped at `offset + fetch` only because the outer `Sort` reorders and re-selects after merging. QED’s model is bag-semantic and has no meaningful `Sort`/`Limit`/`Offset` semantics, so this equivalence cannot be universally proven even if a Sort builder were added to the DSL.
