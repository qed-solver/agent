# PushLimitIntoCrossJoin

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_limit.rs, lines 275-303
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness depends entirely on row-position semantics — a Limit retaining the first skip+fetch rows of a stream, plus the cross-join's stream prefix lying inside the cross product of input prefixes — and QED's decision procedure operates on bag semantics where Limit/Offset/Order-By have no modeled meaning (an uninterpreted operator at best). Under bag semantics the two sides are not universally equal (LHS is a fetch-element sub-bag of the full L×R, RHS a sub-bag of a strictly smaller cross product, and uninterpreted Limit applications give no equational power to relate them), so even an extended DSL exposing Sort/Limit (which JSONSerializer can already serialize) would be futile and no non-trivial special case — not even one using empty inputs, since an uninterpreted inner Limit need not preserve emptiness — could be proved; this is a fundamental QED limitation, not a missed encoding. ```
