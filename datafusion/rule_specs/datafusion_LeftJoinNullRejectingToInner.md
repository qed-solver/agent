# Name: LeftJoinNullRejectingToInner
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/eliminate_outer_join.rs

Registered DataFusion optimizer rule this was extracted from: `EliminateOuterJoin`.

A `Filter` above a `LEFT JOIN` (optionally through one or more Projections, see `inline_through_projection`) whose predicate is null-rejecting on the join's RIGHT side (i.e. the predicate evaluates to NULL/false whenever the right side's columns are the synthetic NULLs a LEFT JOIN pads in for an unmatched left row -- see `extract_null_rejecting_sides`) downgrades the join type from LEFT to INNER. This is the `(JoinType::Left, _, true)` arm of the `eliminate_outer` lookup table. Implement only this one (join type, rejecting side) -> new join type fact.
