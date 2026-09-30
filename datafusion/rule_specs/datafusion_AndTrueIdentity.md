# Name: AndTrueIdentity
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (AND family)`.

`true AND A` or `A AND true` rewrites to just `A` -- `true` is AND's identity element. This is the 'true AND A' / 'A AND true' pair of arms in `Simplifier::f_up`'s match, mirroring OrFalseIdentity.
