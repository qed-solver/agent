# Name: ReduceWindowPartitionCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/window.opt

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
