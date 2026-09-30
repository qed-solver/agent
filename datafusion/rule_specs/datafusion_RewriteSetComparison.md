# Name: RewriteSetComparison
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/rewrite_set_comparison.rs

Registered DataFusion optimizer rule this was extracted from: `RewriteSetComparison`.

Rewrites `expr op ANY/ALL (subquery)` (DataFusion's `Expr::SetComparison`) into an equivalent correlated-subquery comparison: `left_expr op subquery_output_column`, where `left_expr` gets an outer-column reference substituted in via `to_outer_reference`. This is the single arm of `rewrite_set_comparison`'s match on `Expr`; every other expression passes through unchanged. Implement specifically this ANY/ALL-desugaring identity, not any other expression rewrite in the file.
