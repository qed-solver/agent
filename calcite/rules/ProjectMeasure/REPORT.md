# ProjectMeasure

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectMeasureRule` variant (not AggregateMeasureRule, AggregateMeasure2Rule, FilterSortMeasureRule, ProjectSortMeasureRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

ProjectMeasureRule is sound only via Calcite's measure algebra — that M2X(M2V(E), SAME_PARTITION(g)) on a row of partition g equals E evaluated over that partition, so the outer SINGLE_VALUE over the group collapses to E's own aggregate — and QED models M2X, M2V, SAME_PARTITION, and SINGLE_VALUE as independent uninterpreted symbols with no axioms relating them to each other or to ordinary aggregates (e.g. SUM). The partition-scoped, window-like meaning of M2X is outside QED's bag-semantics model (the prover is fixed and has no such operator), so no encoding — down to the narrowest special case like E being a plain column over unique groups — can make the two sides theorems: SMT can always instantiate the uninterpreted symbols to a counterexample, and a builder-level DSL extension could not supply the missing measure axioms. ```
