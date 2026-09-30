# Name: EmptyUnionBranchElimination
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/propagate_empty_relation.rs

Registered DataFusion optimizer rule this was extracted from: `PropagateEmptyRelation`.

`LogicalPlan::Union(union)` drops any branch that is a statically-empty `EmptyRelation { produce_one_row: false, .. }`, keeping only the non-empty branches -- if ALL branches were empty the whole Union becomes one EmptyRelation; if exactly ONE survives it replaces the Union directly (wrapped in a schema-matching Projection if needed); otherwise a smaller Union of just the surviving branches remains. Implement the representative 2-branch case: `Union(EmptyRelation, R)` rewrites to `R` (or a schema-matching Projection of `R`).
