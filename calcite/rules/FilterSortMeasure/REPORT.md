# FilterSortMeasure

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 28  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `FilterSortMeasureRule` variant (not AggregateMeasureRule, AggregateMeasure2Rule, ProjectMeasureRule, ProjectSortMeasureRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

The rule's match shape (Filter over Sort) and its documented M2V push-down both rest on row-ordering semantics — filtering preserving the sort's collation order — which QED fundamentally does not model (Sort/Limit carry no bag-semantic meaning, and the DSL core language has no sort operator at all), so the rule's real content could never be verified. Compounding this, the code as given performs no rewrite whatsoever: the guard `condition.equals(filter.getCondition())` is a reflexive self-comparison that is always true, so `onMatch` always returns early, and even the dead body would merely rebuild the identical Filter-over-Sort plan. There is therefore no non-vacuous, provable rewrite to encode.
