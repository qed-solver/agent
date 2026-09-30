# Name: RedundantDistinctElimination
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/replace_distinct_aggregate.rs

Registered DataFusion optimizer rule this was extracted from: `ReplaceDistinctWithAggregate`.

`Distinct::All(input)` where `input`'s schema already carries a `Dependency::Single` functional dependency covering all of `input`'s own columns (e.g. input is already `GROUP BY`'d on exactly these columns, so every row is already unique) rewrites to just `input` -- the DISTINCT is a provable no-op and is dropped entirely. This is the functional-dependency loop inside ReplaceDistinctWithAggregate's `rewrite`, guarded by `dep.mode == Dependency::Single`. Implement only this redundancy-elimination identity, not the LIMIT-1 or general-GROUP-BY arms in the same function.
