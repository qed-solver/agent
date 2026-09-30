# Name: NullAndFalseToFalse
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (AND family)`.

`NULL AND false` or `false AND NULL` rewrites to the literal `false` -- even though one operand is NULL, AND's `false` absorption still applies (this is the AND-specific carve-out analogous to NullAndOrToNull, confirming `false` wins over `NULL` under AND's three-valued semantics). See the `test_simplify_null_and_false` test.
