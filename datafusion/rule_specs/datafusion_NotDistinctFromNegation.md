# Name: NotDistinctFromNegation
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/simplify_exprs.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (NOT-pushdown family)`.

`NOT (a IS DISTINCT FROM b)` rewrites to `a IS NOT DISTINCT FROM b` (and the reverse), pushing the negation into IS [NOT] DISTINCT FROM's own negated form. See `simplify_not_distinct_from`.
