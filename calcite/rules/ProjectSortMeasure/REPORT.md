# ProjectSortMeasure

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 25  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectSortMeasureRule` variant (not AggregateMeasureRule, AggregateMeasure2Rule, ProjectMeasureRule, FilterSortMeasureRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

The rule rewrites Project-over-Sort into Project-over-Sort-over-Project, so both the before and after patterns structurally require a Sort node, but QED fundamentally does not model row-ordering/collation semantics — Sort has no bag-semantic meaning — and the DSL (RelRN) exposes no Sort builder, so no encoding, even one produced by extending the DSL, can be decided. The rule's genuine soundness additionally rests on the order-independence of Calcite's bespoke M2V operator, which QED can only see as an uninterpreted symbol, so even a Sort-free bag-encoding (which is just a trivially-true Project-Project fusion) would not faithfully verify this rule's ordered-list correctness. ```
