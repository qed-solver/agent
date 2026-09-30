# Name: DistinctNoColumnsToLimitOne
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/replace_distinct_aggregate.rs

Registered DataFusion optimizer rule this was extracted from: `ReplaceDistinctWithAggregate`.

`Distinct::All(input)` where `input`'s schema has zero columns to group by (e.g. `SELECT DISTINCT` on a zero-column relation) rewrites to `Limit { skip: None, fetch: Some(1), input }` instead of an Aggregate with no group keys, since there is either no output row or exactly one (identical) empty row. This is the `group_expr.is_empty()` special case at the top of ReplaceDistinctWithAggregate's `rewrite`.
