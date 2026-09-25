# AggregateCaseToFilter

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 26  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateCaseToFilterRule.java
```

## Independent verifier review

**Verdict:** AGREE

Every shape of this rule (A1/A2/B/C and the DISTINCT form) rewrites to an aggregate call carrying a SQL FILTER argument, but RuleScript's AggCall record has no filter field — Aggregate.semantics() hardcodes a null filter into RelBuilder and JSONSerializer emits no filter for group functions at all — so QED never sees it, and the only alternative encodings are semantically wrong: a Filter below the aggregate drops groups having no matching row, and a join of two aggregates would require the unmodeled fact that the uninterpreted aggregate of an empty bag is NULL. On top of that, QED models aggregation as an uninterpreted function over its input bag (with only the built-in non-NULL filter), knows no algebra of specific aggregates (needed for the SUM0→COUNT and COUNT-of-literal shapes), and the scalar language has no CASE/NULL expression, so the before-side CASE-NULL generation is inexpressible as well — meaning no narrower special case of this rule is provable either. ```
