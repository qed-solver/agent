# Name: MergeNestedLimits
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_limit.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownLimit`.

`Limit(skip: s1, fetch: f1, input: Limit(skip: s2, fetch: f2, input: X))` -- a Limit directly wrapping another Limit -- combines into a single `Limit(skip: combined_skip, fetch: combined_fetch, input: X)` via `combine_limit`, dropping the redundant inner Limit node entirely. This is the 'Merge the Parent Limit and the Child Limit' block at the top of `rewrite_limit`. Implement only the two-nested-literal-Limits merge identity.
