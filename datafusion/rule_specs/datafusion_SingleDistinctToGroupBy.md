# Name: SingleDistinctToGroupBy
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/single_distinct_to_groupby.rs

Registered DataFusion optimizer rule this was extracted from: `SingleDistinctToGroupBy`.

`LogicalPlan::Aggregate` whose aggregate calls all share exactly one common DISTINCT argument expression (`is_single_distinct_agg`, no `GROUPING SETS`) rewrites to a NESTED aggregate: an inner `Aggregate` grouping by `(original group_expr, the shared distinct argument)` with no aggregate calls (deduplicating the distinct argument per original group), wrapped by an outer `Aggregate` grouping by just the original `group_expr` that re-runs each original aggregate call (now non-DISTINCT, since the inner GROUP BY already deduplicated) over the inner aggregate's rows, plus a `Projection` on top to restore the original column names/COUNT-of-empty-input semantics. This turns `SELECT a, COUNT(DISTINCT b) FROM t GROUP BY a` into a query that doesn't need a DISTINCT-aware physical aggregate operator at all. Implement the representative single-group-key, single-COUNT(DISTINCT) case.
