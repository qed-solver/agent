# Name: FullJoinBothNullRejectingToInner
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/eliminate_outer_join.rs

Registered DataFusion optimizer rule this was extracted from: `EliminateOuterJoin`.

A `Filter` above a `FULL JOIN` whose predicate is null-rejecting on BOTH sides downgrades the join type from FULL to INNER. This is the `(JoinType::Full, true, true)` arm of the `eliminate_outer` lookup table. Implement only this one (join type, rejecting sides) -> new join type fact.
