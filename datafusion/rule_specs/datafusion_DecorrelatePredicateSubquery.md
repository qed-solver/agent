# Name: DecorrelatePredicateSubquery
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/decorrelate_predicate_subquery.rs

Registered DataFusion optimizer rule this was extracted from: `DecorrelatePredicateSubquery`.

Rewrites a `Filter`/`Projection` whose predicate/expression contains an `IN (subquery)` or `EXISTS (subquery)` predicate into a semi-join (for the non-negated case) or anti-join (for `NOT IN`/`NOT EXISTS`) between the outer plan and the subquery, pulling any correlated (outer-referencing) predicates from inside the subquery up into the new join's ON condition. See `rewrite_filter_subqueries`/`rewrite_inner_subqueries`/`build_join` for the exact mechanics. Implement the core IN-subquery-to-semi-join identity (uncorrelated or singly-correlated on an equality predicate is a reasonable narrowed scope).
