# Name: NotEqBoolLiteralToOperand
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (Eq/NotEq boolean-literal)`.

`bool_lit != A` or `A != bool_lit` rewrites to: `NOT A` if the literal is `true`; `A` if the literal is `false`; a NULL literal if the literal side is itself NULL -- the mirror of EqBoolLiteralToOperand for `!=` instead of `=`. This is the 'Rules for NotEq' pair of arms in `Simplifier::f_up`'s match.
