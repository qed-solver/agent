# Name: NotLikeToNotLike
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/simplify_exprs.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (NOT-pushdown family)`.

`NOT (x LIKE pattern)` rewrites to `x NOT LIKE pattern` (and similarly `NOT (x ILIKE pattern)` to `x NOT ILIKE pattern`), pushing the negation into LIKE's own negated form. See `simplify_not_like` / `simplify_not_ilike`.
