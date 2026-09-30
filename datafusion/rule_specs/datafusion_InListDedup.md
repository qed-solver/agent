# Name: InListDedup
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (IN-list)`.

`x IN (v1, v2, ..., vn)` where the value list contains literal duplicates deduplicates the list (via `simplify_inlist_set_operation`'s underlying set construction), producing the same membership test with a shorter list. See `simplify_inlist`.
