# PushLimitIntoCrossJoin

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 27  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_limit.rs, lines 275-303
```

## Independent verifier review

**Verdict:** AGREE

The cross-join branch rewrites `LIMIT_n(L ⋈cross R)` to `LIMIT_n(LIMIT_n(L) ⋈cross LIMIT_n(R))`, whose correctness is a purely prefix/enumeration-order argument (the first n output pairs of the nested-loop product draw only on the first n rows of each input, under a specific row-major output order). QED decides equivalence under bag semantics only and, per its documented failure breakdown, does not model the ordering semantics of Sort/Limit/Offset — so even though the JSON format can carry a limit-shaped node (LogicalSort fetch/offset), the unmodifiable prover cannot decide such queries, and no bag-semantic stand-in (uninterpreted filter or projection symbol) can capture prefix selection, since QED cannot reason about predicate entailment or order at all. The limitation sits in the prover itself rather than in a missing DSL feature, so `extend_dsl_file` cannot close the gap and the UNSUPPORTED conclusion stands. ```
