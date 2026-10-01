# PushLimitIntoUnionBranches

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 8  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_limit.rs, lines 145-154
```

## Independent verifier review

**Verdict:** AGREE

This rule is valid only because DataFusion’s union preserves branch order and Limit selects a row-position prefix, so capping each branch to `fetch + skip` rows preserves the outer `Limit(skip, fetch)` result. QED uses bag semantics and explicitly has no ordering semantics for Sort/Limit/Offset, and RuleScript provides no order-preserving limit operator; even if a Limit builder were added, the prover would still treat it as uninterpreted and could not prove the prefix argument.
