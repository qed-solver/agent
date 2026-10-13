# ReduceDecimals

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ReduceDecimalsRule.java
```

## Independent verifier review

**Verdict:** AGREE

Every branch of ReduceDecimalsRule (scale alignment via ×10^k, decode/encode, ROUND_HALF_UP rounding through CASE, overflow checks, REINTERPRET∘REINTERPRET elimination) is only valid under a specific algebra between numeric operators — but QED encodes all scalar operators as uninterpreted symbols with no numeric-arithmetic axioms, so any instance of the rule, down to the simplest special cases like `REINTERPRET(REINTERPRET(x)) = x` or `CAST_dec→int(x) = DIV(x, 10^scale)`, asks the SMT solver to prove an identity between independent uninterpreted functions, which is genuinely invalid. This is a case of "an operator whose specific internal semantics QED cannot see through as an uninterpreted function": the rewrite's premise is the adapter's physical unscaled-BIGINT representation of DECIMAL — the exact reason Calcite itself deprecates the rule as producing non-logical plans — and closing it would require modifying the QED prover, not extending the DSL, so UNSUPPORTED is correct. ```
