# PushLimitIntoLeftJoin

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_limit.rs, lines 275-303
```

## Independent verifier review

**Verdict:** AGREE

QED models Limit (like Sort/Offset) as an uninterpreted QOp with no ordering, row-count, or top-N axioms, so `Limit(n, LeftJoin(L, R, on))` and `Limit(n, LeftJoin(Limit(n, L), R, on))` are structurally distinct uninterpreted terms that the SMT check refutes rather than equates. The rule's soundness rests precisely on ordered top-N semantics — in a left join each left row expands to one or more consecutive output rows, so the first N results can only involve the first N left rows — which the prover's bag semantics cannot express at all. No `extend_dsl_file` change can help because the missing axioms live in the QED prover itself (the trusted, unmodifiable arbiter), and a narrower provable special case is unreachable since any Limit-pushdown variant keeps structurally different Limit terms; this matches the already-recorded CockroachDB `PushLimitIntoJoinLeft` skip. ```
