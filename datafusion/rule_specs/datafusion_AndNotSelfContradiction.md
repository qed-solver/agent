# Name: AndNotSelfContradiction
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (AND family)`.

`A AND NOT A` or `NOT A AND A` where `A` is provably non-nullable rewrites to the literal `false` (law of non-contradiction). This is the 'A AND !A' / '!A AND A' pair of arms in `Simplifier::f_up`'s match, mirroring OrNotSelfTautology.
