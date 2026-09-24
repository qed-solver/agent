# ProjectSortMeasure

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 90  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectSortMeasureRule` variant (not AggregateMeasureRule, AggregateMeasure2Rule, ProjectMeasureRule, FilterSortMeasureRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

This rule rewrites Project(Sort(R)) into Project(Sort(Project(R))), so both patterns structurally require a Sort node, and per QED's own evaluation (as stated in the reference) it does not model list/ordering semantics — Sort (like Limit/Offset/Window/Sample) has no bag-semantic meaning, so no encoding, even one that added a Sort builder to the DSL, could be decided. The rewrite's soundness also rests on the Calcite-internal M2V operator being order-independent, which is precisely the kind of bespoke backend-operator internal semantics QED cannot see through as an uninterpreted symbol. Since every instance of this rule contains a Sort in its before/after shapes, no provable narrower special case of the same rule exists — the porter's UNSUPPORTED conclusion is correct (it merely crashed on a context-limit error before articulating the limitation, which is what should be recorded). ```
