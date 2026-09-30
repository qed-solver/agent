# Name: BinaryOpNullPropagation
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (null propagation)`.

`left OP right` where `OP.returns_null_on_null()` (true for most operators -- arithmetic, comparison, bitwise -- false for a few like AND/OR which have their own 3-valued short-circuit rules) and either `left` or `right` is a NULL literal rewrites to a NULL literal of the expression's own result type. This is the first arm of `Simplifier::f_up`'s match, parametrized over every null-propagating operator -- implement one representative operator (e.g. `x * NULL --> NULL`) and note in SCOPE that it generalizes to any `returns_null_on_null` operator.
