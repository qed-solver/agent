# Name: DeMorganNotAnd
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/simplify_exprs.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (NOT-pushdown family)`.

`NOT (a AND b)` rewrites to `(NOT a) OR (NOT b)` (De Morgan's law). See `simplify_not_bool_and` / `test_simplify_by_de_morgan_laws`.
