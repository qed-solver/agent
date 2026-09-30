# Name: LiteralIsNullFold
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (IS NULL / IS UNKNOWN)`.

`literal IS NULL` or `literal IS NOT NULL` (the operand is a compile-time-known literal, not a column reference) folds directly to the literal boolean `true`/`false` matching whether that literal is actually NULL. See `simplify_expr_is_null` / `simplify_expr_is_not_null`.
