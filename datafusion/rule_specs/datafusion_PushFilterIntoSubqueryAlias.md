# Name: PushFilterIntoSubqueryAlias
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_filter.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownFilter`.

`Filter(predicate, input: SubqueryAlias(alias))` commutes to `SubqueryAlias(Filter(predicate rewritten to alias.input's un-aliased columns, alias.input), alias.alias)` -- a Filter can move below a pure renaming wrapper once its column references are translated back to the inner (un-aliased) schema. This is the `LogicalPlan::SubqueryAlias` arm.
