# Name: SimplifyUdafToScalar
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (misc: casts, structs, UDAF)`.

A user-defined aggregate function call whose arguments are all compile-time constants and whose input relation is provably a single row folds to a plain scalar expression instead of an aggregate call. See `test_simplify_udaf_to_non_aggregate_expr`.
