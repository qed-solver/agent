# Name: AndSelfIdempotent
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (AND family)`.

`(..A..) AND A` or `A AND (..A..)` where the AND-tree on one side already syntactically contains the expression on the other side as one of its own conjuncts collapses to just the side that already contains it. This is the '(..A..) AND A' / 'A AND (..A..)' pair of arms in `Simplifier::f_up`'s match, mirroring OrSelfIdempotent.
