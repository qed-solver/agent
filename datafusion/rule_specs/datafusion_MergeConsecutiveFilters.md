# Name: MergeConsecutiveFilters
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_filter.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownFilter`.

`Filter(predicate: p1, input: Filter(predicate: p2, input: X))` -- a Filter directly atop another Filter -- merges into a single `Filter(p1 AND p2, X)` (via recursing `self.rewrite` on the combined predicate). This is the `LogicalPlan::Filter` arm of PushDownFilter's post-simplification match.
