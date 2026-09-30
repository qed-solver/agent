# Name: OrTrueAbsorption
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (OR family)`.

`true OR A` or `A OR true` (either side is the literal `true`) rewrites to the literal `true`, regardless of whether `A` is nullable or volatile (OR short-circuits on a true operand even if the other side is NULL). This is the 'true OR A' / 'A OR true' pair of arms in `Simplifier::f_up`'s match.
