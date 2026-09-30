# Name: NegateComparisonOperator
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/simplify_exprs.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (NOT-pushdown family)`.

`NOT (a op b)` where `op` is a binary comparison operator (`=`, `!=`, `<`, `<=`, `>`, `>=`) rewrites to `a inverse-op b` (e.g. `NOT (a = b) --> a != b`, `NOT (a < b) --> a >= b`) -- pushing the negation into the comparison by flipping its operator, for the non-nullable case (a nullable comparison's negation must stay guarded, since `NOT NULL` is still NULL, not the flipped comparison's result). See `simplify_not_binary`.
