# Name: OrAndAbsorption
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (OR family)`.

`A OR (A AND B)` or `(A AND B) OR A` (absorption law) collapses to just `A` -- if `A` is true the whole OR is true regardless of `B`; if `A` is false the `A AND B` term is also false, so the OR again reduces to `A`'s own value. This is the 'A OR (A AND B)' / '(A AND B) OR A' pair of arms in `Simplifier::f_up`'s match.
