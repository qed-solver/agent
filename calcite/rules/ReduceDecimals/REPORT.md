# ReduceDecimals

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ReduceDecimalsRule.java
```

## Independent verifier review

**Verdict:** AGREE

ReduceDecimals is a representation-level decimal expansion rule whose correctness depends on Calcite’s decimal scale/precision, REINTERPRET/decode/encode, casts, rounding, and numeric operator semantics. QED models such backend-specific operators only as uninterpreted symbols and can only prove bag-semantic equivalence under that abstraction, so it cannot validate the decimal-to-bigint/double rewrites.
