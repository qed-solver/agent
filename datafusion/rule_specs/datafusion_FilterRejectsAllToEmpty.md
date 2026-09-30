# Name: FilterRejectsAllToEmpty
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/eliminate_filter.rs

Registered DataFusion optimizer rule this was extracted from: `EliminateFilter`.

`LogicalPlan::Filter(Filter { predicate, input, .. })` whose predicate provably always evaluates to FALSE/NULL for every row (via `simplify_filter_predicate`'s `FilterPredicate::RejectsAll` outcome -- e.g. the predicate is the literal `false`, or an AND-tree where one conjunct is trivially false) rewrites to `EmptyRelation { produce_one_row: false, schema: input.schema() }`, replacing the whole Filter+input subtree with a statically-empty relation of the same schema. This is the `FilterPredicate::RejectsAll` arm of EliminateFilter's `rewrite`. Implement only this always-false-filter-to-empty identity, not the always-true or general-simplify arms in the same match.
