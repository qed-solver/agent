# Name: CapSortFetchWithLimit
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_limit.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownLimit`.

`Limit(skip, fetch, input: Sort(sort))` caps `sort.fetch` at `min(sort.fetch.unwrap_or(infinity), skip + fetch)` (a Sort that already had its own smaller fetch is left alone); when `skip > 0` the outer Limit is kept (to skip past the first `skip` of the now-capped Sort's output), otherwise (`skip == 0`) the Limit is dropped entirely since the Sort's own `fetch` now enforces the same cap. This is the `LogicalPlan::Sort` arm of `rewrite_limit`'s inner match. Implement the `skip == 0` sub-case (Limit fully absorbed into Sort.fetch) as the cleanest representative instance.
