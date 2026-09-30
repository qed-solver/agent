# Name: OrFalseIdentity
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (OR family)`.

`false OR A` or `A OR false` (either side is the literal `false`) rewrites to just `A` -- `false` is OR's identity element. This is the 'false OR A' / 'A OR false' pair of arms in `Simplifier::f_up`'s match.
