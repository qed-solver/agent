# Name: PushLimitIntoTableScan
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_limit.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownLimit`.

`Limit(skip, fetch, input: TableScan(scan))` pushes `fetch + skip` down into `scan.fetch` (taking the min with any pre-existing scan-level fetch), letting the table provider itself stop producing rows early, while the Limit node is kept above (skip still needs to happen in the execution layer) if the scan's fetch actually changed. This is the `LogicalPlan::TableScan` arm of `rewrite_limit`'s inner match.
