# DistinctNoColumnsToLimitOne

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 28  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/replace_distinct_aggregate.rs, lines 89-100
```

## Independent verifier review

**Verdict:** AGREE

The rule's RHS is `LIMIT 1`, and QED fundamentally has no bag-semantic model for Limit — per qed.pdf it is only expressible as the uninterpreted QOp(Limit, n, Q) (§3.1) and is explicitly unsupported for its ordering semantics (§6.2) — so in the SMT encoding it is a fresh unconstrained relation, and no equivalence involving it can be proved universally; a `limit()` builder via extend_dsl_file could not help, since the obstacle is the untouchable prover itself, not the DSL. On a zero-column bag, the only core-language construct matching the RHS's "zero or one empty row" semantics is set self-intersection, which is exactly the LHS, so the sole provable encoding degenerates to a vacuous before==after identity rather than a faithful encoding of the DataFusion rewrite. The porter correctly identified that the gap is a real QED limitation (order/limit semantics) rather than a missing encoding trick, so UNSUPPORTED is the right call. ```
