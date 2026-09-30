# Name: PushFilterIntoUnnest
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_filter.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownFilter`.

`Filter(predicate, input: Unnest(unnest))` pushes whichever conjuncts of `predicate` do NOT reference any of the columns UNNEST produces down below the Unnest node (conjuncts that DO reference the unnested columns must stay above, since UNNEST changes row cardinality); if no conjuncts are push-able the Filter is left where it is. This is the `LogicalPlan::Unnest` arm. Implement the narrowed 2-conjunct case: one conjunct referencing only pre-UNNEST columns (pushed below), one referencing the unnested column (stays above).
