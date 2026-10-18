# CommuteLimitProjection

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 6  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_limit.rs, lines 186-192
```

## Independent verifier review

**Verdict:** AGREE

Whether read as the fragment actually given (Limit(fetch) over Sort(fetch) being dropped, i.e. TopK pushdown) or as the named Limit/Project commute, the rule's validity is a statement about ordered row prefixes — "the first n rows survive" — and QED decides equivalence only over unordered bags with uninterpreted functions, explicitly modeling no ordering semantics for Sort/Limit/Offset (qed.pdf §6.2), with the Rust prover being the fixed arbiter that cannot be extended. Any encoding in the core language would have to stand in for "select n rows" as an uninterpreted bag-level function, and the required identity (Limit(π(x)) = π(Limit(x)), or Limit(Sort_k(x)) = Sort_k(x)) is not valid for arbitrary uninterpreted functions — projection can change sizes and duplicate rows after the selection, so the SMT solver would refute it — making a non-vacuous RuleScript encoding impossible. The porter's UNSUPPORTED conclusion (and the prior AGREE verdict on the sibling CapSortFetchWithLimit rule) is correct. ```
