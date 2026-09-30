# Name: BitwiseXorByZero
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (arithmetic/bitwise identity elements)`.

`x ^ 0` or `0 ^ x` rewrites to just `x` -- `0` is bitwise-XOR's identity element. See `test_simplify_bitwise_xor_by_zero`.
