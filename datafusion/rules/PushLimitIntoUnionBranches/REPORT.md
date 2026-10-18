# PushLimitIntoUnionBranches

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 30  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_limit.rs, lines 145-154
```

## Independent verifier review

**Verdict:** AGREE

The rewrite is sound only because "Limit" means "take the first skip+fetch rows of a sequence," a prefix/ordering notion that QED's bag semantics fundamentally lacks — Sort/Limit/Offset have no bag-semantic meaning, so Limit can at best be an uninterpreted symbol. For an arbitrary uninterpreted operator L, the identity L(skip,fetch, A∪B) ≡ L(skip,fetch, L(0,skip+fetch,A) ∪ L(0,skip+fetch,B)) is not a theorem (the "truncated branches still contain every row the outer prefix could draw" argument is exactly the ordering argument QED cannot perform), which is consistent with the porter's byte-verified JSON check being rejected with provable=false. No narrower bag-semantics special case recovers the rule's soundness content, and the gap is a fundamental QED limitation rather than a missing DSL builder, so UNSUPPORTED is correct. ```
