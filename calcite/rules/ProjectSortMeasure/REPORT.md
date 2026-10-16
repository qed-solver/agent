# ProjectSortMeasure

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 34  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectSortMeasureRule` variant (not AggregateMeasureRule, AggregateMeasure2Rule, ProjectMeasureRule, FilterSortMeasureRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

The rule's only non-vacuous content is that re-emitting the Sort (identical sortExps/offset/fetch) below the inserted M2V projection preserves sort order and top-k/offset selection, plus that M2V's per-row value is order-independent — QED fundamentally has no ordering semantics (Sort/Limit/Offset have no bag meaning; the prover path treats LogicalSort as identity, so even a DSL Sort builder couldn't express the order claim), and M2V's measure-expansion row-wise behavior is bespoke operator semantics invisible behind an uninterpreted symbol. Any faithful encoding therefore must drop the Sort and reduces to the generic let-binding identity Project[x0, f(g(x2))] ≡ Project[x0, f(z)] over Project[x0, x1, g(x2)], which holds for arbitrary deterministic f, g and verifies nothing specific to this rule — a genuine QED limitation, not an encoding mistake the porter could fix. ```
