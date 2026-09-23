# FilterSortMeasure

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `FilterSortMeasureRule` variant (not AggregateMeasureRule, AggregateMeasure2Rule, ProjectMeasureRule, ProjectSortMeasureRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

The rule matches a `Filter` directly above a `Sort` in order to push down `M2V` (measure-to-value) calls whose values are defined relative to sort position, but QED has no model for list/order semantics (`Sort` has no bag-semantic meaning) and `M2V`/the measure framework carries bespoke backend internal semantics (RelMdMeasure expansion over sorted rows) that QED cannot see through as uninterpreted functions. As written, `onMatch` merely rebuilds the identical `Filter(Sort(X))` tree, so the only encodable form would be a Sort-free tautology that proves nothing about the actual rule — there is no faithful, non-trivial special case to salvage. (The porter's stated reason was only an LLM context-length error, but the UNSUPPORTED conclusion happens to be correct.)
