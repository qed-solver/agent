# Name: PushTopKThroughJoin
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_limit.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownLimit`.

`Sort(sort)` with a literal `fetch` (a 'Top-K' sort: ORDER BY ... LIMIT k with no intervening plain Limit node, or a Limit-absorbed-into-Sort from CapSortFetchWithLimit) whose input is a `Join` pushes the same Top-K cap down onto whichever join input the sort key(s) belong to, when that side alone determines the sort order (see `push_topk_through_join` for the exact single-side-determines-order precondition). Implement the representative case: a Sort-with-fetch directly above a LEFT JOIN, ordering only by the join's LEFT-side columns, pushes its own fetch cap onto the left input.
