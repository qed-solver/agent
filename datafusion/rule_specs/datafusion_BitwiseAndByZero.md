# Name: BitwiseAndByZero
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (arithmetic/bitwise identity elements)`.

`x & 0` or `0 & x` rewrites to the literal `0` -- `0` is bitwise-AND's absorbing element. See `test_simplify_bitwise_and_by_zero`.
