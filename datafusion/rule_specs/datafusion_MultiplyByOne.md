# Name: MultiplyByOne
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (arithmetic/bitwise identity elements)`.

`x * 1` or `1 * x` rewrites to just `x` -- `1` is multiplication's identity element. See `test_simplify_multiply_by_one`.
