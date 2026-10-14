# FilterDateRange

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/DateRangeRules.java
```

## Independent verifier review

**Verdict:** AGREE

FilterDateRange's soundness rests on the concrete calendar semantics of EXTRACT/FLOOR/CEIL and date literals — the claim that {t | EXTRACT(YEAR, t) = 1997} equals {t | t BETWEEN '1997-01-01' AND '1997-12-31'} holds only for that specific operator and those specific computed constants, not for arbitrary uninterpreted functions, so QED (which models all scalar operators/predicates as uninterpreted symbols and cannot reason about predicate entailment between independent symbols or a backend operator's bespoke internal semantics) must refute the faithful two-sided encoding. The transcript's SMT result (provable: false, complete_fragment: true, no timeout) is a genuine decision, not an incompleteness artifact, and the only encoding QED could accept would reuse one predicate symbol on both sides — a tautology that is not the rule. The gap is date/calendar theory in the unmodifiable Rust prover, not a missing DSL builder, so extend_dsl_file cannot close it and the UNSUPPORTED conclusion is correct. ```
