# Name: FilterAcceptsAllToNoop
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/eliminate_filter.rs

Registered DataFusion optimizer rule this was extracted from: `EliminateFilter`.

`LogicalPlan::Filter(Filter { predicate, input, .. })` whose predicate provably always evaluates to TRUE for every row (via `simplify_filter_predicate`'s `FilterPredicate::AcceptsAll` outcome -- e.g. the predicate is the literal `true`, or an OR-tree where one disjunct is trivially true) rewrites to just `input`, dropping the Filter node entirely. This is the `FilterPredicate::AcceptsAll` arm of EliminateFilter's `rewrite`. Implement only this always-true-filter-elision identity, not the always-false or general-simplify arms in the same match.
