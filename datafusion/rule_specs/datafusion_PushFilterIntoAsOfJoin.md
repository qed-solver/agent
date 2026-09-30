# Name: PushFilterIntoAsOfJoin
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_filter.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownFilter`.

`Filter(predicate, input: AsOfJoin(join))` (DataFusion's `ASOF JOIN`, an inequality-based temporal join) pushes whichever conjuncts of `predicate` reference only the join's LEFT side down into `Filter(left)` (the right side of an AsOfJoin cannot be filtered ahead of the as-of matching, unlike a regular join). This is the `LogicalPlan::AsOfJoin` arm. Implement the narrowed single-left-only-conjunct case.
