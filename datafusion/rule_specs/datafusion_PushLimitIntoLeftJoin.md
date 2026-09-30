# Name: PushLimitIntoLeftJoin
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_limit.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownLimit`.

`Limit(skip, fetch, input: Join(join))` where `join.join_type` is LEFT pushes a `Limit(0, fetch + skip, ..)` onto only the LEFT input (the side LEFT JOIN always fully preserves) -- the right side cannot be capped since a single left row can still need to match against arbitrarily many right rows. This is the `Left => (Some(limit), None)` arm of `push_down_join`. Implement only the left-side-capped identity; PushLimitIntoRightJoin covers the mirror case.
