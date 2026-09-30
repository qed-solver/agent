# Name: FullJoinLeftNullRejectingToLeft
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/eliminate_outer_join.rs

Registered DataFusion optimizer rule this was extracted from: `EliminateOuterJoin`.

A `Filter` above a `FULL JOIN` whose predicate is null-rejecting on the LEFT side only downgrades the join type from FULL to LEFT (the right side's null-padding, which is what makes FULL different from LEFT, is now provably filtered out on the left side, but the right side's own null-padding for unmatched right rows is still needed). This is the `(JoinType::Full, true, false)` arm of the `eliminate_outer` lookup table. Implement only this one (join type, rejecting side) -> new join type fact.
