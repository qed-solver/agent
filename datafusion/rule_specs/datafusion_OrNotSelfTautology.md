# Name: OrNotSelfTautology
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (OR family)`.

`A OR NOT A` or `NOT A OR A` where `A` is provably non-nullable rewrites to the literal `true` (excluded middle) -- guarded by non-nullability since `NULL OR NOT NULL` is `NULL OR NULL` which is NULL, not true, per SQL's three-valued logic. This is the 'A OR !A' / '!A OR A' pair of arms in `Simplifier::f_up`'s match.
