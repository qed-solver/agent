# Name: ModuloByOne
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (arithmetic/bitwise identity elements)`.

`x % 1` rewrites to the literal `0` (of the expression's result type) -- any integer modulo 1 is always exactly zero. See `test_simplify_modulo_by_one` / `test_simplify_modulo_by_one_non_null`.
