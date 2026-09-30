# Name: PushLimitIntoCrossJoin
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_limit.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownLimit`.

`Limit(skip, fetch, input: Join(join))` where `join` is a cross join (INNER with no `on` keys and no filter) pushes a `Limit(0, fetch + skip, ..)` onto BOTH the left and right inputs (each input independently can be capped, since a cross join needs at most `fetch+skip` rows from EITHER side to produce `fetch+skip` output rows). This is the `is_cross_join(&join)` branch of `push_down_join`. Implement only the cross-join both-sides-capped identity.
