# FilterSortMeasure

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 8  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `FilterSortMeasureRule` variant (not AggregateMeasureRule, AggregateMeasure2Rule, ProjectMeasureRule, ProjectSortMeasureRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

As written, Calcite's `FilterSortMeasureRule` is dead code: `onMatch` assigns `condition = filter.getCondition()` and then guards on `condition.equals(filter.getCondition())` — a reflexive self-comparison on the same object that is always true, so it always returns; even the unreachable branch merely rebuilds the identical `Filter(cond, Sort(R))` plan, so there is no substantive transformation to verify. The rule's matching shape (Filter over Sort, in service of the order-dependent M2V-pushdown intent seen in the sibling `ProjectSortMeasureRule`) fundamentally requires row-order semantics, which QED explicitly does not model (Sort/Limit have no bag-semantic meaning) and RuleScript's core language exposes no Sort operator — dropping the Sort yields only the vacuous identity `Filter(p, R) ≡ Filter(p, R)`, which certifies nothing.
