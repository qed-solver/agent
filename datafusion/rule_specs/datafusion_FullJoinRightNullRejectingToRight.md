# Name: FullJoinRightNullRejectingToRight
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/eliminate_outer_join.rs

Registered DataFusion optimizer rule this was extracted from: `EliminateOuterJoin`.

A `Filter` above a `FULL JOIN` whose predicate is null-rejecting on the RIGHT side only downgrades the join type from FULL to RIGHT (mirror of FullJoinLeftNullRejectingToLeft). This is the `(JoinType::Full, false, true)` arm of the `eliminate_outer` lookup table. Implement only this one (join type, rejecting side) -> new join type fact.
