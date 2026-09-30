# Name: PushFilterIntoWindow
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_filter.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownFilter`.

`Filter(predicate, input: Window(window))` where every column `predicate` references is one of `window`'s PARTITION BY columns (not a window-function output) pushes the Filter below the Window -- a predicate that only constrains the partition key is constant within each partition, so filtering before windowing removes whole partitions and produces the same surviving rows' window values as filtering after. This is the `LogicalPlan::Window` arm, guarded by the predicate referencing only partition-by columns (mirrors cockroach's already-ported PushSelectIntoWindow -- reuse the same join-back-on-partition-key encoding idiom from that rule).
