# Name: PushFilterIntoUnionBranches
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_filter.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownFilter`.

`Filter(predicate, input: Union(union))` pushes a COPY of `predicate` onto every branch of the union (`Filter(predicate, branch)` for each), since filtering the union of several relations is equivalent to unioning the filtered relations. This is the `LogicalPlan::Union` arm.
