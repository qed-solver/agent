# Name: LimitZeroToEmpty
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/eliminate_limit.rs

Registered DataFusion optimizer rule this was extracted from: `EliminateLimit`.

`LogicalPlan::Limit(limit)` with a literal `fetch` of exactly `0` rewrites to `EmptyRelation { produce_one_row: false, schema: limit.input.schema() }` -- LIMIT 0 always produces zero rows regardless of the input, so the whole subtree collapses to a statically-empty relation. This is the `v == 0` arm inside EliminateLimit's `rewrite`. Implement only this fetch-zero-to-empty identity, not the no-op-limit-removal arm in the same function.
