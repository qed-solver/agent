# CapSortFetchWithLimit

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_limit.rs, lines 161-185
```

## Independent verifier review

**Verdict:** AGREE

The Sort arm of DataFusion's push_down_limit only changes the numeric fetch/skip parameters on Sort/Limit (capping the sort's fetch at skip+fetch), and its correctness is purely a TopK-prefix statement about which rows come "first" in sort order — rows beyond position skip+fetch can never survive LIMIT/OFFSET. QED decides equivalence only over unordered bags with uninterpreted functions and has no Sort/Limit/Offset operators or ordering semantics (qed.pdf: it "does not support queries that make use of the ordering semantics of Limit, Offset, or Order By"), and adding a Java sort/limit builder via extend_dsl_file could not help since the Rust prover — the fixed arbiter — has no order model to apply. Any approximation that replaces "first k sorted rows" with an uninterpreted filter predicate would encode arbitrary sub-bags rather than sorted prefixes and be rightly refuted, while dropping the Sort/Limit from both sides leaves input ≡ input, which is vacuous; so no non-vacuous RuleScript encoding exists. ```
