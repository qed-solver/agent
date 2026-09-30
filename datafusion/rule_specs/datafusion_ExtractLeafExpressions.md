# Name: ExtractLeafExpressions
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/extract_leaf_expressions.rs

Registered DataFusion optimizer rule this was extracted from: `ExtractLeafExpressions`.

Pass 1 of a two-pass pair (paired with PushDownLeafProjections). For an `Aggregate`/`Filter`/`Sort`/`Limit`/`Join` node whose own expressions contain a 'complex leaf' sub-expression (one that only references a single input column plus constants, e.g. `get_field(x, 'a')` or `x + 1`, as opposed to referencing multiple columns), extracts that sub-expression out into a new aliased column (`__datafusion_extracted_N`) computed by an inserted `Projection` immediately below the node, and rewrites the node's own expression to reference the new column instead -- setting up for PushDownLeafProjections to then push that extraction Projection further down, closer to the base scan (so e.g. `get_field` can be evaluated once near the source instead of being recomputed per Aggregate/Filter/Sort row it flows through). See `extract_from_plan`. Implement the representative Filter case: `Filter(get_field(x, 'a') = 5, ...)` extracts `get_field(x,'a')` into a Projection column below the Filter.
