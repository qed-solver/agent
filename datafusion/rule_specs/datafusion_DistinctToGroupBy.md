# Name: DistinctToGroupBy
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/replace_distinct_aggregate.rs

Registered DataFusion optimizer rule this was extracted from: `ReplaceDistinctWithAggregate`.

`Distinct::All(input)` (general case: nonzero columns, not already proven unique) rewrites to `Aggregate(input, group_expr: expand_wildcard(input.schema()), aggr_expr: [])` -- SELECT DISTINCT becomes a GROUP BY over every output column with zero aggregate calls. This is the fallthrough case at the bottom of ReplaceDistinctWithAggregate's `rewrite`, after the LIMIT-1 and functional-dependency special cases are ruled out.
