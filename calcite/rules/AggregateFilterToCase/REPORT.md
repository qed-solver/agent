# AggregateFilterToCase

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateFilterToCaseRule.java
```

## Independent verifier review

**Verdict:** AGREE

The identity f(x) FILTER (WHERE p) ≡ f(CASE WHEN p THEN x END) (including the COUNT() no-arg special case) is valid only by algebra of specific aggregate functions — null-ignoring aggregation over the filtered row subset vs. aggregation over a CASE column that yields NULL on non-matching rows, plus filter/grouping commutation — and QED explicitly knows nothing about an aggregate's algebra beyond input bag equality, so it cannot relate the two uninterpreted aggregate applications whose input bags differ. No DSL extension rescues this: the JSON serializer's `group` node doesn't even carry `filterArg` (and there is no CASE/NULL in the core language), so expressing the filtered side would just lose the filter to the prover rather than reveal the null/CASE interaction it models. There is no non-trivial special case (e.g. restricting to COUNT or a tautological filter) that avoids needing the prover to know null-counting/CASE semantics, so UNSUPPORTED is the correct, fundamental conclusion — the porter's context-length crash merely short-circuited what any real attempt would have reached. ```
