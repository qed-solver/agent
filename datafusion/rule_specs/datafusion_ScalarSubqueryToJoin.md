# Name: ScalarSubqueryToJoin
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/scalar_subquery_to_join.rs

Registered DataFusion optimizer rule this was extracted from: `ScalarSubqueryToJoin`.

Rewrites a `Filter` whose predicate embeds a scalar subquery (a subquery expected to return exactly one row/column, e.g. `WHERE x = (SELECT ... )`) into a LEFT JOIN between the outer plan and the (at-most-one-row) subquery result, with the subquery's output column substituted for the scalar-subquery expression in the remaining filter predicate. See `build_join` for the exact join construction. Implement the core scalar-subquery-to-left-join identity for the simplest case: an uncorrelated or single-equality-correlated scalar subquery compared with `=`.
