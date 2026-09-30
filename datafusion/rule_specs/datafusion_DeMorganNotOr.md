# Name: DeMorganNotOr
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/simplify_exprs.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (NOT-pushdown family)`.

`NOT (a OR b)` rewrites to `(NOT a) AND (NOT b)` (De Morgan's law, dual of DeMorganNotAnd). See `simplify_not_bool_or`.
