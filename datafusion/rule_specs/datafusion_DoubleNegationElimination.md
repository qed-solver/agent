# Name: DoubleNegationElimination
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/simplify_exprs.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (NOT-pushdown family)`.

`NOT (NOT x)` rewrites to just `x`, for ANY inner shape of `x` (a plain boolean column, `IS NULL`, `IN (...)`, `BETWEEN`, `LIKE`, `ILIKE`, `IS DISTINCT FROM`, `IN (subquery)`, `EXISTS`, ...) -- this single identity is exercised by every `simplify_not_not_*` test in simplify_exprs.rs (e.g. `simplify_not_not`, `simplify_not_not_null`, `simplify_not_not_in`, `simplify_not_not_between`, `simplify_not_not_like`, `simplify_not_not_ilike`, `simplify_not_not_distinct_from`, `simplify_not_not_in_list`, `simplify_not_not_exists`, `simplify_not_not_in_subquery`) but is really one identity, not ten -- implement the representative plain-boolean case `NOT (NOT x) --> x`.
