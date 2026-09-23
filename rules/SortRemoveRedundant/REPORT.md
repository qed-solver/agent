# SortRemoveRedundant

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortRemoveRedundantRule.java
```

## Independent verifier review

**Verdict:** AGREE

This rule rewrites `Sort` nodes in both their ORDER BY and LIMIT forms, and QED explicitly does not model list/ordering semantics — `Sort`/`Limit`/`Offset` have no bag-semantic meaning — so the core claims (an ORDER BY is removable when the input has ≤1 row; a LIMIT is removable when the input has ≤fetch rows) are about row order and list truncation, which the prover cannot decide. Adding a `Sort`/`sortLimit` builder to `RelRN` via `extend_dsl_file` would not close the gap, since the limitation is in the prover's semantics rather than the DSL surface, and the JSON serializer's `LogicalSort` support is only about emitting plans QED still cannot reason about. Independently of that, the rule is not a universal equivalence: its validity rests on the optimizer-metadata side condition "input's max row count ≤ threshold," which cannot be expressed as a precondition on uninterpreted relations in RuleScript, so even the special cases (e.g. over a group-less aggregate) cannot be faithfully stated and proved.
