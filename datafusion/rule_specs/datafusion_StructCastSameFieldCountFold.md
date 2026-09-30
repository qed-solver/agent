# Name: StructCastSameFieldCountFold
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (misc: casts, structs, UDAF)`.

`CAST(struct_literal AS StructType)` where the source struct literal and target struct type have the same field count (and compatible per-field types) folds to a struct literal with each field individually cast, rather than a runtime whole-struct CAST. See `test_struct_cast_same_field_count_foldable` / `test_struct_cast_different_names_same_count`.
