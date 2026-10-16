# SortRemoveRedundant

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortRemoveRedundantRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rewrite is conditional on a cost-model fact — the input's max row count being ≤ 1 (ORDER BY) or ≤ fetch (pure LIMIT) — and RuleScript patterns are unconditional before/after pairs with no hypothesis mechanism and no cardinality-bounded input (the `unique` flag encodes key/functional dependency, not a row bound), so any encoding that drops the precondition is simply unsound in general. Even the one structurally encodable instance, a Sort/Limit over the `empty` relation, is unprovable because QED assigns Sort/Limit/Offset no ordering or bag semantics at all — they are uninterpreted QOps whose outputs become unconstrained during normalization, making Sort(R) ≡ R reduce to "fresh unconstrained variable ≡ R". Adding a Sort builder via `extend_dsl_file` would not help, since the JSON format already carries sorts while the prover itself models them uninterpreted — the gap is a fundamental QED limitation, exactly the one the reference lists (Sort/Limit/Offset/Window/Sample have no bag-semantic meaning). ```
