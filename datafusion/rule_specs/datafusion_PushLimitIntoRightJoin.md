# Name: PushLimitIntoRightJoin
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_limit.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownLimit`.

`Limit(skip, fetch, input: Join(join))` where `join.join_type` is RIGHT pushes a `Limit(0, fetch + skip, ..)` onto only the RIGHT input, mirroring PushLimitIntoLeftJoin. This is the `Right => (None, Some(limit))` arm of `push_down_join`.
