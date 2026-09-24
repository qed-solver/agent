# FilterMultiJoinMerge

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterMultiJoinMergeRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule rewrites the *internal* post-join filter state of Calcite's MultiJoin — a backend-specific N-ary join operator (joinFilter + postJoinFilter + per-pair outer-join conditions + optional projFields) whose semantics live inside the operator, and which has no node in QED's core language or its JSON format (only binary joins; the Rust prover, the trusted arbiter, cannot be modified to accept a multi-join node, and a DSL extension could at best desugar it back to core nodes). Uninterpreted symbols exist only for tables/predicates/projections/join-kinds/types — not for relational operators — so MultiJoin cannot be introduced as an opaque symbol whose inner state is shared between before() and after() and conjunctively enriched on the after side. The only available encoding flattens the inner-join/no-projection MultiJoin into nested true-joins plus stacked filters, which reduces the rule to the plain FilterMerge identity already in the core language (trivially provable, but it verifies nothing about MultiJoin itself — the flattening itself would be an unverified assumption); the outer-join and projFields variants cannot even be flattened, since QED cannot relate a predicate over a projected row to its translation below an uninterpreted projection. ```
