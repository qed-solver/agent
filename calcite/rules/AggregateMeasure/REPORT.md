# AggregateMeasure

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 7  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `AggregateMeasureRule` variant (not AggregateMeasure2Rule, ProjectMeasureRule, FilterSortMeasureRule, ProjectSortMeasureRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

The rewrite is valid only because Calcite’s measure operators satisfy a specialized algebraic identity supplied by RelMdMeasure, namely that an AGG_M2V aggregate over a group equals SINGLE_VALUE of the per-row M2X(c, SAME_PARTITION(group)) expansion. QED treats aggregate functions and scalar operators as uninterpreted, with no axiom mechanism to encode that identity, so a faithful RuleScript encoding would leave a required equality between distinct uninterpreted aggregate/term structures that the prover cannot derive.
