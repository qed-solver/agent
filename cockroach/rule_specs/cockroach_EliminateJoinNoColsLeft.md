# Name: EliminateJoinNoColsLeft
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

EliminateJoinNoColsLeft eliminates an InnerJoin with a one row, zero column
left input set. These can be produced when a Values, scalar GroupBy, or other
one-row operator's columns are never used.

Extracted from `join.opt` (which defines multiple rules — implement specifically `EliminateJoinNoColsLeft`, not the other rules in that file):

```
# EliminateJoinNoColsLeft eliminates an InnerJoin with a one row, zero column
# left input set. These can be produced when a Values, scalar GroupBy, or other
# one-row operator's columns are never used.
[EliminateJoinNoColsLeft, Normalize]
(InnerJoin | InnerJoinApply
    $left:* &
        (ColsAreEmpty (OutputCols $left)) &
        (HasOneRow $left)
    $right:*
    $on:*
)
=>
(Select $right $on)
```
