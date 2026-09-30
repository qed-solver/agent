# Name: ExtractEquijoinPredicate
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/extract_equijoin_predicate.rs

Registered DataFusion optimizer rule this was extracted from: `ExtractEquijoinPredicate`.

`LogicalPlan::Join` with a non-empty `filter` expression (a residual ON-clause predicate not already expressed as `on` equi-pairs) splits that filter via `split_eq_and_noneq_join_predicate` into equi-join predicates (moved into `on`) and any remaining non-equi predicate (kept as `filter`) -- hoisting `l.c1 = r.c1 AND l.c2 > r.c2` out of a generic filter-join into `on: [(l.c1, r.c1)], filter: l.c2 > r.c2`, enabling a hash-join instead of a nested-loop join. Also handles `IS NOT DISTINCT FROM` as a null-safe equi-predicate via `NullEquality`. Implement the core equality-hoisting identity.
