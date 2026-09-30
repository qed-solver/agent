# Name: CaseBranchAlwaysFalseElided
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (CASE)`.

A `CASE WHEN c1 THEN t1 WHEN c2 THEN t2 ... END` where one `WHEN ci` condition (other than the first) is the literal `false`/`NULL` drops that one branch entirely from the CASE (it can never fire), leaving the remaining branches in order. See `simplify_expr_case_when_any_false`.
