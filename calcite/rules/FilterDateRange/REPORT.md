# FilterDateRange

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/DateRangeRules.java
```

## Independent verifier review

**Verdict:** AGREE

FilterDateRange's soundness rests on the calendar rounding semantics of EXTRACT/FLOOR/CEIL — that `extract(unit, ts) = c` holds iff `ts` falls in a concrete boundary interval — but QED models those date-time functions only as uninterpreted function symbols over bag semantics, and the core language offers no way to introduce the date literals or any axioms/defined semantics tying an extraction function to interval bounds. For an uninterpreted `extract`, the implication `extract(x) = c ⟹ lo <= x < hi` fails under some instantiations, so SMT will always produce a countermodel; a "provable" encoding would have to equate the two sides via the same independent symbol and would no longer be this rule. This matches the "operator's bespoke internal semantics" limitation, not an encoding bug — note the porter also never actually attempted an encoding (the transcript ends in a context-length API error). ```
