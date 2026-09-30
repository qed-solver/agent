# Name: CastLiteralFold
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (misc: casts, structs, UDAF)`.

`CAST(literal AS T)` where the source literal can be exactly represented in target type `T` folds at plan-build time to the already-cast literal value directly, avoiding a runtime CAST evaluation on every row. See `simplify_cast_literal` / `simplify_fixed_size_binary_eq_lit`.
