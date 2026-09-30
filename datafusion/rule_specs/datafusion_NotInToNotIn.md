# Name: NotInToNotIn
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/simplify_exprs.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (NOT-pushdown family)`.

`NOT (x IN (v1, ..., vn))` rewrites to `x NOT IN (v1, ..., vn)` (pushing the negation into the IN-list's own negated form rather than leaving it wrapped in a separate NOT). See `simplify_not_in` / `simplify_not_in_list`.
