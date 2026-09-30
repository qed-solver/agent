# Name: UnwrapSingletonUnion
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/optimize_unions.rs

Registered DataFusion optimizer rule this was extracted from: `OptimizeUnions`.

`LogicalPlan::Union(Union { inputs, .. })` with exactly one input rewrites to that single input directly (a UNION of one branch is that branch). This is the first arm of OptimizeUnions's `rewrite` match, guarded by `inputs.len() == 1`. Implement only this one-input-unwrap arm, not the other Union-handling arms in the same match.
