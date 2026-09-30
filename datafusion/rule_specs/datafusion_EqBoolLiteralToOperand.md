# Name: EqBoolLiteralToOperand
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (Eq/NotEq boolean-literal)`.

`bool_lit = A` or `A = bool_lit` (one side is a literal `true`/`false`/`NULL`, the other a boolean-typed expression) rewrites to: `A` if the literal is `true`; `NOT A` if the literal is `false`; a NULL literal if the literal side is itself NULL. This is the 'Rules for Eq' pair of arms in `Simplifier::f_up`'s match (one arm per literal-on-left vs literal-on-right, same identity by commutativity -- implement either side as the representative instance).
