# Name: NotInSubqueryToNotInSubquery
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/simplify_exprs.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (NOT-pushdown family)`.

`NOT (x IN (subquery))` rewrites to a `NOT IN (subquery)`-marked expression directly (mirroring NotExistsToNotExists but for IN-subquery), so downstream decorrelation recognizes it as an anti-join candidate. See `simplify_not_in_subquery`.
