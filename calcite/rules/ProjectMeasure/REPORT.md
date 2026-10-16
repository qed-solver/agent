# ProjectMeasure

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 41  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectMeasureRule` variant (not AggregateMeasureRule, AggregateMeasure2Rule, FilterSortMeasureRule, ProjectSortMeasureRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

ProjectMeasureRule's rewrite (Aggregate(g, SINGLE_VALUE(d)) over a Project with d = M2X(V2M(e)) AS d) is only sound because of the measure-algebra identity M2X(V2M(x, SAME_PARTITION(g))) = x plus partition-constancy of measures, but in QED's model M2X/V2M/SAME_PARTITION are uninterpreted scalar symbols and SINGLE_VALUE is an uninterpreted aggregate known only up to bag equality of its input, so no RuleScript encoding or DSL builder extension (the format has no facility for function-level axioms) can make the SMT prover accept the equivalence for all instantiations.
