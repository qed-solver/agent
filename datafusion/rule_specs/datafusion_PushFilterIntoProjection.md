# Name: PushFilterIntoProjection
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_filter.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownFilter`.

`Filter(predicate, input: Projection(projection))` where every column `predicate` references corresponds to a simple pass-through (non-computed, or safely-inlinable) projection expression rewrites to `Projection(Filter(predicate-with-projection-exprs-inlined, projection.input), projection.expr)` -- pushing the filter below the Projection and inlining the projection's expression definitions into the filter predicate wherever the filter referenced a computed (non-column) projection output. This is the `LogicalPlan::Projection` arm. Implement the narrowed case where the Projection is a pure column subset/reorder (no computed expressions), so no inlining is needed.
