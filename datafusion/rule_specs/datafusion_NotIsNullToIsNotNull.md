# Name: NotIsNullToIsNotNull
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/simplify_exprs.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (NOT-pushdown family)`.

`NOT (x IS NULL)` rewrites to `x IS NOT NULL` (and, symmetrically, `NOT (x IS NOT NULL)` rewrites to `x IS NULL`). See `simplify_not_null`.
