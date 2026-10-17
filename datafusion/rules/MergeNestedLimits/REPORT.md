# MergeNestedLimits

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_limit.rs, lines 93-120
```

## Independent verifier review

**Verdict:** AGREE

MergeNestedLimits is only correct under positional/ordering semantics — which rows survive `skip`+`fetch` depends on row order — and QED explicitly gives Sort/Limit/Offset no bag-semantic meaning, treating them as uninterpreted free operators with no axioms. Consequently `Limit(s,f,Limit(s',f',R))` and `Limit(s+s',min(f,f'),R)` are non-congruent free-term applications that SMT can never equate, and no constant choice or DSL builder extension can fix this, since even degenerate cases like limit idempotence (`Limit(k,R) ≡ Limit(k,Limit(k,R))`) would require limit axioms that the fixed prover does not and cannot have. ```
