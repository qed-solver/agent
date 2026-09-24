# AggregateMeasure

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `AggregateMeasureRule` variant (not AggregateMeasure2Rule, ProjectMeasureRule, FilterSortMeasureRule, ProjectSortMeasureRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness is exactly the identity AGG_M2V(c) over group G = SINGLE_VALUE of the per-row M2X(c, SAME_PARTITION(G)) — an algebraic relationship between two *distinct* aggregate functions, plus the coupling of the SAME_PARTITION scalar expression to the aggregate's grouping — that Calcite supplies via RelMdMeasure metadata, i.e. a backend operator's bespoke internal semantics. QED models every aggregate call as an uninterpreted group function and "knows nothing about a specific aggregate function's algebra beyond bag equality of its input," so it can only equate identical calls over identical input bags and will countermodel any faithful encoding; the DSL already exposes every shape needed (scanMany, ProjectMany, generic projection/aggregate symbols, multi-key Aggregate), so this is a fundamental QED limitation rather than a missing builder, and the only "provable" version would alias the symbols into a vacuous tautology that no longer states the rule. (The porter's stated reason was actually an HTTP 400 context-overflow crash before any test, but the UNSUPPORTED conclusion itself is sound.) ```
