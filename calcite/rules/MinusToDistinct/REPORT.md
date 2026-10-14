# MinusToDistinct

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 10  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MinusToDistinctRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rewrite's correctness rests entirely on reading membership off aggregate outputs — `COUNT() FILTER tag=i` > 0 / == 0 testing whether a grouped row came from each MINUS input — but QED models aggregate calls as uninterpreted functions of their input bag (special-casing only the COUNT(*)-vs-COUNT(col) non-NULL prefilter), so it can never connect those comparisons to set-difference membership. This is a fundamental prover limitation, not a missing builder: even adding a FILTER field to `RelRN.AggCall` (or re-encoding it as COUNT(CASE...)) would still leave the count values opaque, and no 2-branch special case can avoid the existence test, so no narrower provable encoding of this specific rule exists. ```
