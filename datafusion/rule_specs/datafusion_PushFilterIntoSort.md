# Name: PushFilterIntoSort
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_filter.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownFilter`.

`Filter(predicate, input: Sort(sort))` commutes to `Sort(Filter(predicate, sort.input))` -- filtering doesn't depend on row order, so it can move below a Sort with no change in the final (filtered, then still sorted) result, and now the Sort itself has fewer rows to order. This is the `LogicalPlan::Sort` arm.
