# ProjectMeasure

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectMeasureRule` variant (not AggregateMeasureRule, AggregateMeasure2Rule, FilterSortMeasureRule, ProjectSortMeasureRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

ProjectMeasureRule's soundness rests on Calcite's measure-calculus law — that SINGLE_VALUE(M2X(M2V(E), SAME_PARTITION(g))) aggregated over a g-partition collapses to the measure value E (e.g. SUM(c)+1) computed over that partition — which is a bespoke internal semantic identity, not a bag-semantic equivalence. QED treats M2V/M2X/V2M/SAME_PARTITION and the aggregates as uninterpreted symbols with only functional-consistency, so it cannot see through or relate them (an adversary can instantiate M2X to a constant to break it), and the before-plan's measure-bearing Project has no clean per-row core-language interpretation to model in the first place. This is a genuine QED limitation (uninterpretable measure/aggregate algebra) rather than a missing builder that extend_dsl_file could close, so no full or narrowed faithful encoding is provable.
