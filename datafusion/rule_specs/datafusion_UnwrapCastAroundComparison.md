# Name: UnwrapCastAroundComparison
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/unwrap_cast.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions submodule: unwrap_cast.rs`.

`CAST(column AS WiderType) op literal` (a comparison between a column cast to a wider numeric/string/timestamp type and a literal) rewrites to `column op CAST(literal AS column's original narrower type)` -- moving the CAST from the (row-evaluated) column side to the (once-evaluated) literal side -- whenever the literal's value is exactly representable in the column's original, narrower type (see `is_supported_type` and the per-type exact-representability checks). When the literal does NOT fit in the narrower type (e.g. comparing a widened `i32` column to a literal outside `i32`'s range), the CAST is intentionally left un-unwrapped since the comparison result would otherwise change. Implement the representative case: `CAST(int_col AS BIGINT) = 5i64` (5 fits in i32) rewrites to `int_col = 5i32`; see `test_unwrap_cast_comparison` and the negative-control precedent in `test_not_unwrap_cast_comparison`.
