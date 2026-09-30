# Name: RemoveNoopLimit
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/eliminate_limit.rs

Registered DataFusion optimizer rule this was extracted from: `EliminateLimit`.

`LogicalPlan::Limit(limit)` with `fetch: None` and a literal `skip` of exactly `0` rewrites to just `limit.input` -- a LIMIT clause with no row cap and no rows skipped has no effect and is removed entirely. This is the `fetch.is_none() && skip == 0` arm inside EliminateLimit's `rewrite`. Implement only this no-op-limit-removal identity, not the fetch-zero-to-empty arm in the same function.
