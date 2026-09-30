# Name: EmptyInListToFalseOrNull
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (IN-list)`.

`x IN ()` (an empty value list) rewrites to the literal `false` when `x` is provably non-nullable, or to a NULL literal when `x` may itself be NULL (since `NULL IN ()` is NULL, not false, under SQL's three-valued logic -- an empty IN-list can never be satisfied, but a NULL `x` still yields an unknown/NULL result rather than a definite false). See `simplify_null_in_empty_inlist` / `just_simplifier_simplify_null_in_empty_inlist`.
