# Name: DivideBySelf
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (arithmetic/bitwise identity elements)`.

`x / x` (syntactically identical, non-volatile `x`) rewrites to the literal `1` when `x` is provably non-nullable and non-zero; guarded appropriately for the nullable/possibly-zero case. See `test_simplify_divide_by_same`.
