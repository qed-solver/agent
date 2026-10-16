# ReduceWindowPartitionCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 0  **Verification rounds used:** 0
**Scope detail:** the window is modeled as one uninterpreted per-partition aggregate function joined back to its input, and the functional dependency is the syntactic one where the redundant partition column is an uninterpreted function f of the retained partition column k


## Source rule (as given to the porter)

```
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
```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 21247790
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 31800417
  },
  "smt_timed_out": false,
  "nontrivial_perms": true,
  "translate_duration": {
    "secs": 0,
    "nanos": 164250
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1150209
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 39798875
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 32381666
  },
  "total_duration": {
    "secs": 0,
    "nanos": 76089583
  }
}
```
