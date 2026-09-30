# Name: DedupeGroupByExprs
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/eliminate_duplicated_expr.rs

Registered DataFusion optimizer rule this was extracted from: `EliminateDuplicatedExpr`.

`LogicalPlan::Aggregate(agg)` with 2+ `group_expr` entries deduplicates the GROUP BY list (removing syntactic repeats via an `IndexSet`, same mechanism as DedupeSortExprs but for `agg.group_expr`), rebuilding the Aggregate with the deduplicated key list while keeping `aggr_expr` untouched. This is the `LogicalPlan::Aggregate` arm of EliminateDuplicatedExpr's `rewrite`. Implement only the GROUP-BY-list deduplication, not the Sort arm in the same file.
