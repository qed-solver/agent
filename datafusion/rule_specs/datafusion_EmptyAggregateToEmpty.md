# Name: EmptyAggregateToEmpty
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/propagate_empty_relation.rs

Registered DataFusion optimizer rule this was extracted from: `PropagateEmptyRelation`.

`LogicalPlan::Aggregate(agg)` over an `EmptyRelation { produce_one_row: false, .. }` input rewrites to `EmptyRelation` of the aggregate's own output schema -- UNLESS `agg.group_expr` contains an empty grouping set (from `GROUPING SETS(())`, `ROLLUP`, or `CUBE`, which always emit exactly one row even over empty input, so must NOT be elided). See `has_empty_grouping_set`. Implement the core case: a plain (non-grouping-set) GROUP BY over an EmptyRelation input collapses to EmptyRelation.
