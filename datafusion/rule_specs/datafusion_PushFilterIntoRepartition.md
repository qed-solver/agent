# Name: PushFilterIntoRepartition
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_filter.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownFilter`.

`Filter(predicate, input: Repartition(repartition))` commutes to `Repartition(Filter(predicate, repartition.input))` -- a Filter can always move below a pure data-redistribution node with no output-shape change. This is the `LogicalPlan::Repartition` arm.
