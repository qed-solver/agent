# Name: EmptyOptionalJoinSideToNullPadded
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/propagate_empty_relation.rs

Registered DataFusion optimizer rule this was extracted from: `PropagateEmptyRelation`.

`LogicalPlan::Join(join)` where a side that JOIN semantics *preserves via null-padding* when absent (an outer-join's non-required side) is a statically-empty `EmptyRelation` rewrites the Join to a `Projection` over the surviving side that keeps its own columns as-is and CASTs the empty side's columns to `NULL` of their declared type (see `build_null_padded_projection`). Concretely: Full with only the right (or only the left) side empty; Left with the right side empty; Right with the left side empty; LeftAnti with the right side empty (surviving side passes through unchanged, no NULL columns since LeftAnti's output schema is only the left side's); RightAnti with the left side empty (mirror). Implement one representative instance, e.g. `Left join with an empty right side rewrites to a null-padded Projection over the left side`, and note in SCOPE the other listed (JoinType, side) combinations this generalizes to.
