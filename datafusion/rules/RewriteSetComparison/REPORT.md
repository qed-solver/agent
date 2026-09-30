# RewriteSetComparison

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 101  **Verification rounds used:** 6

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/rewrite_set_comparison.rs, lines 1-175
```

## Independent verifier review

**Verdict:** AGREE

The rule rewrites set-comparison predicates (`= ANY`, `> ALL`) into CASE expressions built from EXISTS subqueries with SQL three-valued logic (IS NULL / IS TRUE / IS FALSE on a boolean comparison). RuleScript's predicate language is restricted to uninterpreted atomic symbols combined with boolean operators (And/Or/Not/True/False); it has no construct for embedding a subquery or correlated EXISTS inside a filter predicate, no CASE/WHEN scalar expression, and no three-valued-logic (NULL-propagating) comparison semantics. The before pattern `Filter(x = ANY (SELECT y FROM T), R)` and the after pattern `Filter(CASE WHEN EXISTS(...) THEN true WHEN EXISTS(...) IS NULL THEN null ELSE false END, R)` both require predicate-level constructs that simply do not exist in the DSL, and even if they were added as uninterpreted symbols, QED's bag-semantic SMT framework cannot reason about predicate-level logical entailment between independent symbols or about the algebraic relationship between ANY/ALL quantification and the EXISTS+CASE decomposition with null handling. ```
