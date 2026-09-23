# FilterDateRange

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/DateRangeRules.java
```

## Independent verifier review

**Verdict:** AGREE

FilterDateRangeRule's soundness rests on the specific calendar/timestamp semantics of EXTRACT/FLOOR/CEIL — e.g. that `EXTRACT(YEAR FROM d) = v` holds exactly over the interval [v-01-01, (v+1)-01-01) — which requires axioms relating those functions to comparisons on `d` that RuleScript/QED have no mechanism to state. The DSL can't express either side structurally (no date literals, no calendar functions), so both sides reduce to independent uninterpreted predicate symbols over the same column, and QED cannot infer entailment between independent symbols or see through an operator's internal semantics. The only provable encoding is the trivial identity (same predicate name on both sides), so the rule is genuinely outside RuleScript/QED's capability, not a failure of encoding. ```
