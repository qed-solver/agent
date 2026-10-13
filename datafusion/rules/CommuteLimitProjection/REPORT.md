# CommuteLimitProjection

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_limit.rs, lines 186-192
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests on Limit's ordered-prefix semantics — *which* rows are retained depends on row order — and QED's bag-semantic model fundamentally has no Sort/Limit/Offset or ordering structure, so that premise cannot be stated. Encoding Limit as an uninterpreted operator gives no axiom linking its output to its input, so `Limit(f,s,Proj(E,R)) = Proj(E,Limit(f,s,R))` is not first-order valid (e.g. a LIMIT that returns one fixed row whenever its input is nonempty is a countermodel for any non-fixing projection E). The only special cases collapse: identity/permutation with unbounded fetch is vacuous, and fetch=0 would require the unmodeled cardinality axiom Limit(0)=∅, so no non-trivial provable special case exists. ```
