# AggregateMeasure2

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 26  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `AggregateMeasure2Rule` variant (not AggregateMeasureRule, ProjectMeasureRule, FilterSortMeasureRule, ProjectSortMeasureRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

The rule's only non-trivial content is the Calcite measure-framework identity that AGG_M2V(c) over a group equals RelMdMeasure's expansion — a correlated subquery built from *different* operators (AGG_M2M, M2X/M2V, measure-specific functions), all of which become distinct uninterpreted symbols in RuleScript. QED's sole aggregate principle is bag-equality of inputs for the *same* operator; it has no axioms relating different operator symbols, so the SMT layer finds countermodels for every instance (even the minimal one-key, one-AGG_M2V case), and no DSL extension (e.g. a scalar-subquery builder) could close this gap without hard-coding the measure's definitional semantics as an axiom, which would make the proof vacuous. ```
