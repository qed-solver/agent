# PushTopKThroughJoin

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 30  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_limit/topk_through_join.rs, lines 92-308
```

## Independent verifier review

**Verdict:** AGREE

This rule's only content is that the first-N-rows-under-a-sort-key of a LEFT/RIGHT join are sourced from the first-N-rows of the preserved input (with sort keys confined to that side), which is pure ordered/top-N stream semantics; QED models all queries in bag semantics and treats Sort/Limit/Order-By as uninterpreted QOps with no ordering or top-N axioms (qed.pdf §6.2, "List semantics"), so `Sort(fetch=N, Join(L,R))` and `Sort(fetch=N, Join(Sort(fetch=N,L), R))` are structurally distinct uninterpreted terms SMT cannot equate. Dropping the Sort wrappers degenerates to the vacuous identity `Join(L,R) ≡ Join(L,R)`, no non-vacuous fragment reduces to a bag equality (even with a unique key or fetch=1, "the N-smallest" needs an order QED doesn't model), and `extend_dsl_file` can't close the gap because the missing axioms live in the unmodifiable prover, which serializes but does not axiomatize ordering — the same structural situation as the already-confirmed `SortJoinTranspose`/`PushLimitIntoLeftJoin`/`PushLimitIntoJoinLeft` skips. ```
