# Name: MultiplyByZero
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (arithmetic/bitwise identity elements)`.

`x * 0` or `0 * x` rewrites to the literal `0` (of the expression's result type) when `x` is provably non-nullable (if `x` could be NULL, `x * 0` must stay NULL-propagating rather than folding to a non-null zero). See `test_simplify_multiply_by_zero`.
