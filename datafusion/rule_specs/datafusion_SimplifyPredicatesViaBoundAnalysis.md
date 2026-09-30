# Name: SimplifyPredicatesViaBoundAnalysis
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/simplify_predicates.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions submodule: simplify_predicates.rs`.

Within an AND-conjunction of comparison predicates over the same column (e.g. `x > 5 AND x < 10 AND x != 3`), analyzes each conjunct as a bound on that column's possible value range and: drops any conjunct whose bound is already implied by a tighter conjunct elsewhere in the same AND (`test_equality_subsumes_predicates_it_satisfies`), or replaces the WHOLE conjunction with the literal `false` if two conjuncts describe disjoint/unsatisfiable ranges (`test_disjoint_bounds_are_unsatisfiable`). See `test_satisfiable_bounds_are_kept` for the case where no simplification applies. Implement the representative subsumption case: `x > 5 AND x > 3` simplifies to just `x > 5`.
