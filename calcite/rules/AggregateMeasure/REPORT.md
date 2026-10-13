# AggregateMeasure

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 47  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `AggregateMeasureRule` variant (not AggregateMeasure2Rule, ProjectMeasureRule, FilterSortMeasureRule, ProjectSortMeasureRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

The rule's validity rests on Calcite's measure invariants — that a measure is constant within the group-key partition and that AGG_M2V(c) coincides with SINGLE_VALUE(M2X(c, SAME_PARTITION(keys))) — which QED cannot model: every aggregate (AGG_M2V, SINGLE_VALUE) and scalar op (M2X, SAME_PARTITION) is an uninterpreted symbol with no axioms (in particular no "single_value over a group-constant bag returns that value" law), and the partition-constancy invariant is not expressible as a table constraint in the DSL. Hence the prover's counterexample is a genuine one in QED's semantics, not an encoding artifact — the two sides name different aggregate symbols over related bags, which no name-matching fix can align — and since the rule's firing condition is at least one AGG_M2V call whose replacement inherently introduces the SINGLE_VALUE/M2X construction, no non-vacuous special case avoids the barrier, so UNSUPPORTED is correct. ```
