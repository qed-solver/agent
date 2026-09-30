# Name: PushFilterIntoTableScan
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_filter.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownFilter`.

`Filter(predicate, input: TableScan(scan))` splits `predicate`'s conjuncts by whether the underlying `TableProvider` reports it can evaluate them itself (`TableProviderFilterPushDown::Exact`/`Inexact` via `scan.source.supports_filters_pushdown`); Exact-supported conjuncts move into `scan.filters` and are dropped from the Filter above entirely, Inexact-supported ones are copied into `scan.filters` AND kept above (belt-and-suspenders, since the provider may not fully enforce them), Unsupported ones stay only in the Filter above. This arm is provider-dependent (opaque `TableProvider` behavior) and not expected to be provable in a relational-algebra-only model like RuleScript/QED -- included here for completeness of the source's own rule inventory, not because it's expected to port.
