# ReduceWindowPartitionCols

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 29  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/window.opt

ReduceWindowPartitionCols reduces a set of partition columns to a simpler form
using FDs. Window partition columns are redundant if they are functionally
determined by other window partition columns.

Extracted from `window.opt` (which defines multiple rules — implement specifically `ReduceWindowPartitionCols`, not the other rules in that file):

```
# ReduceWindowPartitionCols reduces a set of partition columns to a simpler form
# using FDs. Window partition columns are redundant if they are functionally
# determined by other window partition columns.
[ReduceWindowPartitionCols, Normalize]
(Window
    $input:*
    $fn:*
    $private:* &
        ^(ColsAreEmpty
            $redundantCols:(RedundantCols
                $input
                (WindowPartition $private)
            )
        )
)
=>
(Window
    $input
    $fn
    (RemoveWindowPartitionCols $private $redundantCols)
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests on functional-dependency inference — that partitioning by (k, f(k)) induces the same partitions as partitioning by k — which QED cannot derive because it models aggregation opaquely per key tuple and has no attribute/FD reasoning (the porter's minimal GroupBy(k,f(k))-vs-GroupBy(k) probe, which is semantically valid since f is a deterministic uninterpreted function, is rejected before SMT even runs). Independently, the Window operator itself (especially with ORDER BY clauses and frames, as the sibling rules in window.opt show) has no bag-semantic model in QED, and no Java-side DSL extension can add that reasoning to the unmodifiable prover core. ```
