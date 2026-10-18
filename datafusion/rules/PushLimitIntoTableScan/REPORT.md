# PushLimitIntoTableScan

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_limit.rs, lines 128-144
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests on row-order/scan-prefix semantics — LIMIT/OFFSET selects particular rows of an unordered scan, and TableScan.skip/fetch returns such a prefix — but QED's trusted bag-semantic model explicitly gives no semantics to Sort/Limit/Offset, and `RelRN` exposes no limit/offset builder (adding one to the DSL could not supply semantics the fixed Rust prover lacks). The before's full scan and the after's trimmed scan would be two unrelated opaque scan symbols whose prefix relation is unexpressible in the JSON format, and under strict bag equality the two plans aren't even equivalent (DataFusion treats unordered LIMIT output as an arbitrary subset, a coarser equivalence than bag equality), so no non-trivial special case could be proven either. ```
