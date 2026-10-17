# PushLimitIntoRightJoin

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_limit.rs, lines 275-303
```

## Independent verifier review

**Verdict:** AGREE

PushLimitIntoRightJoin is sound only because of a top-n/prefix ordering property — the first n rows of a RIGHT-join output can only involve the first ≤ n rows of the preserving (right) input — but QED works in pure bag semantics and encodes Limit as an uninterpreted QOp with no ordering, sub-bag, or idempotence axioms, so `Limit(n, RJoin(L,R))` and `Limit(n, RJoin(L, Limit(n,R)))` are two distinct uninterpreted terms the SMT solver can never equate. No DSL extension can close the gap (the immutable Rust prover itself is what lacks limit/ordering semantics per qed.pdf §6.2), and no non-trivial special case — unique-key inputs, skip=0, constant n — removes the dependence on top-n behavior, matching the independently verified skip of the structurally identical CockroachDB PushLimitIntoJoinRight.
