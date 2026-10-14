# ReduceDecimals

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ReduceDecimalsRule.java
```

## Independent verifier review

**Verdict:** AGREE

Every sub-rewrite in this rule — including the simplest one, `REINTERPRET(REINTERPRET(x)) → x` — rests on a value-level identity between decimal arithmetic and its unscaled-BIGINT expansion (10^scale scaling, ROUND_HALF_UP via CASE/DIVIDE_INTEGER, precision/overflow bounds, and the physical REINTERPRET type reinterpretation), while QED only supplies bag algebra over uninterpreted scalar symbols with every virtual type lowered to a plain integer and no arithmetic semantics on any operator. Consequently no faithful before/after pair is decidable: an honest encoding is either two independent uninterpreted terms (unprovable) or the identical term on both sides (degenerate, not a rule), and `extend_dsl_file` cannot close the gap because the unmodifiable prover would still treat any new CAST/REINTERPRET/arithmetic builders as uninterpreted black boxes, and encoding REINTERPRET as a bare type identity would silently alter the semantics rather than capture the rule. ```
