# Name: PushFilterIntoAggregate
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_filter.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownFilter`.

`Filter(predicate, input: Aggregate(agg))` where every column `predicate` references is one of `agg`'s GROUP BY key columns (not an aggregate-call output) pushes the Filter below the Aggregate -- filtering on grouping columns before or after aggregation produces the same result, and filtering first means fewer rows get aggregated. This is the `LogicalPlan::Aggregate` arm, guarded by the predicate referencing only group-by columns. Implement the narrowed single-group-key case.
