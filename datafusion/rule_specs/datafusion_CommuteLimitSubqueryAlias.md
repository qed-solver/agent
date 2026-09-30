# Name: CommuteLimitSubqueryAlias
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_limit.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownLimit`.

`Limit(skip, fetch, input: SubqueryAlias(alias))` commutes to `SubqueryAlias(Limit(skip, fetch, alias.input), alias.alias)` -- a LIMIT above a pure renaming wrapper can move below it unchanged. This is the `LogicalPlan::SubqueryAlias` arm of `rewrite_limit`'s inner match.
