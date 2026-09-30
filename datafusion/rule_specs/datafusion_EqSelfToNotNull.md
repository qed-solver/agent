# Name: EqSelfToNotNull
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (Eq/NotEq boolean-literal)`.

`A = A` where the two occurrences are syntactically identical AND `A` is a non-volatile expression rewrites to: the literal `true` if `A` is provably non-nullable; otherwise `A IS NOT NULL OR NULL` (capturing SQL's three-valued-logic rule that `NULL = NULL` is NULL, not true). This is the 'A = A' arm of `Simplifier::f_up`'s match, guarded by `!left.is_volatile()` (a volatile expression like `random()` is NOT self-equal after resimplification, since each evaluation differs).
