# Name: EmptyRequiredJoinSideToEmpty
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/propagate_empty_relation.rs

Registered DataFusion optimizer rule this was extracted from: `PropagateEmptyRelation`.

`LogicalPlan::Join(join)` where a side that JOIN semantics *requires* to produce output (the side is not null-padded when absent) is a statically-empty `EmptyRelation` rewrites the whole Join to `EmptyRelation` of the join's own schema. Concretely (see the `match join.join_type` table in the source): Inner with either side empty; Full with BOTH sides empty; Left with the left side empty; Right with the right side empty; LeftSemi/RightSemi with either side empty; LeftAnti with the left side empty; RightAnti with the right side empty. Implement one representative instance, e.g. `Inner join with an empty left side rewrites to EmptyRelation`, and note in SCOPE that the same identity covers the other listed (JoinType, side) combinations.
