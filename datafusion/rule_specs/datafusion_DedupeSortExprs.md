# Name: DedupeSortExprs
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/eliminate_duplicated_expr.rs

Registered DataFusion optimizer rule this was extracted from: `EliminateDuplicatedExpr`.

`LogicalPlan::Sort(sort)` deduplicates `sort.expr` (removing any ORDER BY expression that is a syntactic repeat of an earlier one in the same list, via an `IndexSet`), keeping only the first occurrence's position/direction and preserving the rest of the list's order. This is the `LogicalPlan::Sort` arm of EliminateDuplicatedExpr's `rewrite`. Implement only the Sort-expression-list deduplication, not the Aggregate arm in the same file.
