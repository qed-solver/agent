# Name: OrCommonFactorDistribution
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (OR family)`.

`(A AND B) OR (A AND C)` where `A` (or some common non-volatile conjunct) appears in both sides' AND-trees factors out into `A AND (B OR C)` (distributive law run in the factoring direction) via `has_common_conjunction`/`iter_conjunction_owned`. This is the 'Eliminate common factors in conjunctions' arm of `Simplifier::f_up`'s match.
