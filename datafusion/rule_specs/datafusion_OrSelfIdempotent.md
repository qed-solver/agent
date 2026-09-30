# Name: OrSelfIdempotent
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (OR family)`.

`(..A..) OR A` or `A OR (..A..)` where the OR-tree on one side already syntactically contains the exact expression on the other side as one of its own disjuncts (see `expr_contains`) collapses to just the side that already contains it -- `A OR (A OR B)` simplifies to `A OR B`. This is the '(..A..) OR A' / 'A OR (..A..)' pair of arms in `Simplifier::f_up`'s match.
