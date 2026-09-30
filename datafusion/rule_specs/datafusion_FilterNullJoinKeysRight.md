# Name: FilterNullJoinKeysRight
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/filter_null_join_keys.rs

Registered DataFusion optimizer rule this was extracted from: `FilterNullJoinKeys`.

`LogicalPlan::Join(join)` with non-empty equi-join `on` keys, `null_equality: NullEqualsNothing`, and whose RIGHT side is 'preserved' by the join type gets a `Filter(right, create_not_null_predicate(nullable on-key columns))` inserted directly below the right input for every nullable right-side join key, for the same reason as FilterNullJoinKeysLeft. Implement the right-side half of the rule; FilterNullJoinKeysLeft covers the mirror left-side case in the same function.
