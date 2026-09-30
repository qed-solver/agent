# Name: BitwiseShiftByZeroNoop
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (arithmetic/bitwise identity elements)`.

`x << 0` or `x >> 0` (shifting by a literal zero) rewrites to just `x`. See `test_simplify_bitwise_bitwise_shift_right_by_zero` / `test_simplify_bitwise_bitwise_shift_left_by_zero`.
