# Name: AndOrAbsorption
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (AND family)`.

`A AND (A OR B)` or `(A OR B) AND A` (absorption law, dual of OrAndAbsorption) collapses to just `A`. This is the 'A AND (A OR B)' / '(A OR B) AND A' pair of arms in `Simplifier::f_up`'s match.
