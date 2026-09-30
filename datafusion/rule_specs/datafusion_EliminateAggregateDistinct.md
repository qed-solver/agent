# Name: EliminateAggregateDistinct
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/eliminate_aggregate_distinct.rs

Registered DataFusion optimizer rule this was extracted from: `EliminateAggregateDistinct`.

`LogicalPlan::Aggregate(aggregate)` where EVERY aggregate call in `aggregate.aggr_expr` is DISTINCT and is provably insensitive to duplicate removal for its particular argument (`can_strip_every_distinct` -- e.g. `MIN(DISTINCT x)` == `MIN(x)`, `MAX(DISTINCT x)` == `MAX(x)`; a plain `SUM`/`COUNT` would NOT qualify since duplicates change their result) strips the `DISTINCT` flag from every aggregate call (`strip_insensitive_distinct`), preserving each call's original output name via a `NamePreserver`. Implement the representative case: a single `MIN(DISTINCT x)` aggregate call rewrites to `MIN(x)`.
