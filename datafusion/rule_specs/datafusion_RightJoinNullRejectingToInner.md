# Name: RightJoinNullRejectingToInner
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/eliminate_outer_join.rs

Registered DataFusion optimizer rule this was extracted from: `EliminateOuterJoin`.

A `Filter` above a `RIGHT JOIN` whose predicate is null-rejecting on the join's LEFT side downgrades the join type from RIGHT to INNER. This is the `(JoinType::Right, true, _)` arm of the `eliminate_outer` lookup table. Implement only this one (join type, rejecting side) -> new join type fact.
