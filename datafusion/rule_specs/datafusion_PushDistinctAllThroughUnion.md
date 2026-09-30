# Name: PushDistinctAllThroughUnion
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/optimize_unions.rs

Registered DataFusion optimizer rule this was extracted from: `OptimizeUnions`.

`LogicalPlan::Distinct(Distinct::All(nested_plan))` where `nested_plan` is itself a `LogicalPlan::Union` flattens the nested union's inputs (same `extract_plans_from_union` flattening as FlattenNestedUnions) while keeping the outer `Distinct::All` wrapper -- i.e. `Distinct(Union(Union(a,b), c))` becomes `Distinct(Union(a,b,c))`. This is the third arm of OptimizeUnions's `rewrite` match (nested inside the `Distinct::All` case). Implement only this identity, not the plain union-flattening arm (FlattenNestedUnions) which has no Distinct wrapper.
