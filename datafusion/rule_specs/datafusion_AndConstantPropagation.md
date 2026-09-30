# Name: AndConstantPropagation
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (AND family)`.

In an AND-tree, if one conjunct fixes a column to a specific literal (e.g. `x = 5`), that literal value is substituted for `x` inside the OTHER conjuncts of the same AND-tree before those conjuncts are otherwise simplified (e.g. `x = 5 AND x + 1 > 3` simplifies the second conjunct using `x = 5`). See the `simplify_and_constant_prop` test and its CASE-expression variant. Implement the representative 2-conjunct case.
