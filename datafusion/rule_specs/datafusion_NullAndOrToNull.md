# Name: NullAndOrToNull
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (null propagation)`.

`NULL AND NULL` or `NULL OR NULL` (both operands literal NULL) rewrites to a NULL literal -- a special case carved out because AND/OR do NOT generally satisfy `returns_null_on_null()` (e.g. `false AND NULL --> false`, not NULL), so this narrower both-sides-NULL case needs its own arm ahead of the general AND/OR rules. This is the second arm of `Simplifier::f_up`'s match.
