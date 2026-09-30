# Name: FilterNullJoinKeysLeft
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/filter_null_join_keys.rs

Registered DataFusion optimizer rule this was extracted from: `FilterNullJoinKeys`.

`LogicalPlan::Join(join)` with non-empty equi-join `on` keys, `null_equality: NullEqualsNothing` (standard SQL null-rejecting equality), and whose LEFT side is 'preserved' by the join type (i.e. the join type doesn't already guarantee left rows survive unmatched -- see `on_lr_is_preserved`) gets a `Filter(left, create_not_null_predicate(nullable on-key columns))` inserted directly below the left input for every nullable left-side join key. This lets the physical hash-join skip building/probing with rows that could never match anyway (a NULL join key never equals anything under standard equality). Implement the left-side half of the rule; FilterNullJoinKeysRight covers the mirror right-side case in the same function.
