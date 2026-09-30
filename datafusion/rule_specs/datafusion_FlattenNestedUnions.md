# Name: FlattenNestedUnions
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/optimize_unions.rs

Registered DataFusion optimizer rule this was extracted from: `OptimizeUnions`.

`LogicalPlan::Union(Union { inputs, schema })` (2+ inputs) flattens any input that is itself a Union into this Union's own input list (via `extract_plans_from_union`), so a Union-of-Unions becomes one flat Union, coercing each flattened input's expressions to the parent's schema. This is the second arm of OptimizeUnions's `rewrite` match. Implement only the flattening identity, not the singleton-unwrap or distinct-pushdown arms.
