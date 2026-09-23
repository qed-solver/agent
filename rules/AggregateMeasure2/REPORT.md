# AggregateMeasure2

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `AggregateMeasure2Rule` variant (not AggregateMeasureRule, ProjectMeasureRule, FilterSortMeasureRule, ProjectSortMeasureRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

AggregateMeasure2's validity rests entirely on the Calcite measure-framework identity `AGG_M2V(c) ≡ expand(AGG_M2M(c))`, where `expand` is a `RelMdMeasure` metadata-driven expansion into a correlated `RexSubQuery`. That is an algebraic relation between two *distinct* uninterpreted aggregate symbols (QED only equates an aggregate's input bag; it has no axioms relating different aggregate operators) compounded with correlated-subquery/scoping semantics that QED's semiring model does not capture at all, so no RuleScript encoding — general or narrower special case — is provable.
