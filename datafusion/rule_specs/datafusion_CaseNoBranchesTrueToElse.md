# Name: CaseNoBranchesTrueToElse
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (CASE)`.

A `CASE WHEN c1 THEN t1 ... ELSE e END` where every `WHEN` condition is a literal `false`/`NULL` rewrites to just the `ELSE` expression `e` (or a NULL literal if there is no ELSE) -- no branch can ever fire. See `simplify_expr_case_when_any_true` (the dual: when every condition folds and exactly one is `true`, same underlying single-live-branch mechanism).
