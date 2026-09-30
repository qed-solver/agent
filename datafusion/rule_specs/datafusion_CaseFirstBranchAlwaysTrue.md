# Name: CaseFirstBranchAlwaysTrue
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (CASE)`.

A `CASE WHEN c1 THEN t1 WHEN c2 THEN t2 ... END` whose FIRST `WHEN` condition is the literal `true` rewrites to just `t1` -- every later branch is unreachable since the first condition always fires. See `simplify_expr_case_when_first_true`.
