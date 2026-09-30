# Name: ReorderPredicatesByCost
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/reorder_predicates.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions submodule: reorder_predicates.rs`.

Within an AND-conjunction, reorders the conjuncts so that cheaper-to-evaluate predicates (plain column comparisons) are checked before more expensive ones (e.g. `LIKE` pattern matches), since AND short-circuits on the first false conjunct -- evaluating cheap likely-to-fail predicates first reduces the expected number of expensive evaluations. This does not change the AND's result, only the evaluation order of its conjuncts. See `like_predicate_moves_after_equality` / `order_among_cheap_predicates_is_preserved`.
