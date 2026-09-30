# Name: IsUnknownToIsNull
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (IS NULL / IS UNKNOWN)`.

`A IS UNKNOWN` (where `A` is a boolean-typed expression) rewrites to `A IS NULL` -- for a boolean expression, 'unknown' under three-valued logic and 'is null' are the same predicate. See `simplify_expr_is_unknown` / `simplify_expr_is_not_known`.
