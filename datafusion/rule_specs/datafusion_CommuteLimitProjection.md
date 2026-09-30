# Name: CommuteLimitProjection
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_limit.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownLimit`.

`Limit(skip, fetch, input: Projection(proj))` commutes to `Projection(Limit(skip, fetch, proj.input))` -- a LIMIT above a pure column-selecting Projection can move below it unchanged, since Projection doesn't change row count or order. This is the `LogicalPlan::Projection` arm of `rewrite_limit`'s inner match.
