# Name: EliminateGroupByConstant
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/eliminate_group_by_constant.rs

Registered DataFusion optimizer rule this was extracted from: `EliminateGroupByConstant`.

`LogicalPlan::Aggregate(aggregate)` whose `group_expr` contains an expression that is 'redundant' given the OTHER group-by columns already present (`is_redundant_group_expr` -- the common case being a group-by expression that is itself a *literal constant*, which contributes nothing to grouping granularity) moves that redundant expression out of `group_expr` and into a `Projection` on top instead (so it still appears in the output row with its constant value, but the Aggregate itself groups by fewer, non-redundant keys). Guarded so it never eliminates ALL grouping expressions (which would silently change an empty-input GROUP BY's zero-row result into an ungrouped aggregate's one-row result). Implement the representative case: `GROUP BY a, 5` (one real column, one literal-constant group key) rewrites to `Aggregate(GROUP BY a) -> Projection(a, 5)`.
