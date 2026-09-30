# Name: NotBetweenToOutsideRange
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/simplify_exprs.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (NOT-pushdown family)`.

`NOT (x BETWEEN lo AND hi)` rewrites to `x < lo OR x > hi` (the complement of a closed range is the union of the two open half-ranges outside it). See `simplify_not_between`.
