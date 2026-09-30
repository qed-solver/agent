# Name: DivideByOne
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (arithmetic/bitwise identity elements)`.

`x / 1` rewrites to just `x` -- `1` is division's right-identity element. See `test_simplify_divide_by_one`.
