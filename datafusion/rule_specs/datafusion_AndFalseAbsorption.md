# Name: AndFalseAbsorption
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (AND family)`.

`false AND A` or `A AND false` rewrites to the literal `false`, regardless of whether `A` is nullable or volatile. This is the 'false AND A' / 'A AND false' pair of arms in `Simplifier::f_up`'s match, mirroring OrTrueAbsorption.
