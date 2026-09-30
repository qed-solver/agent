# Name: PushLimitIntoUnionBranches
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_limit.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownLimit`.

`Limit(skip, fetch, input: Union(union))` pushes a `Limit(0, fetch + skip, branch)` onto EVERY branch of the union (each branch alone could contribute up to `fetch + skip` rows to the union's output, even though the union as a whole only needs that many total), keeping the original outer Limit above the (now capped) Union. This is the `LogicalPlan::Union` arm of `rewrite_limit`'s inner match.
