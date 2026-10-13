# AggregateMeasure2

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 25  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `AggregateMeasure2Rule` variant (not AggregateMeasureRule, ProjectMeasureRule, FilterSortMeasureRule, ProjectSortMeasureRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

The rule's validity rests entirely on Calcite's measure invariant — AGG_M2V(m) over a group equals the scalar subquery produced by mq.expand(m) (with AGG_M2M standing in for measure-to-measure references) — while QED models AGG_M2V, AGG_M2M, and any subquery/aggregate body as uninterpreted symbols with no axioms connecting them, so that equality is not a consequence of bag semantics and is not derivable by any DSL extension (a RexSubQuery builder would let the pattern be written but QED would soundly report "not provable" since the two sides are distinct uninterpreted terms). The only non-trivial content of the rewrite is precisely this M2V→subquery replacement; the pass-through of group keys and non-measure agg calls is either trivial or depends on the same measure axiom, so no genuine provable special case exists. ```
