# Name: UnionsToFilter
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/unions_to_filter.rs

Registered DataFusion optimizer rule this was extracted from: `UnionsToFilter`.

`Distinct::All(Union(branches))` where every branch scans the *same* underlying relation with different (mutually exclusive, collectively exhaustive) filter predicates rewrites to `Distinct::All(Filter(underlying_relation, OR-of-branch-predicates))` -- i.e. a UNION of filtered copies of one table collapses into ONE scan filtered by the disjunction of the branch predicates, avoiding redundant re-scans. See `try_rewrite_distinct_union` for the exact matching conditions (same input plan modulo the filter, `NullEquality`/schema compatibility). Implement the core identity: a 2-branch UNION ALL of Filter(R, p1) and Filter(R, p2), both wrapped in DISTINCT, collapses to DISTINCT(Filter(R, p1 OR p2)).
