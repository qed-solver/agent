# Name: EliminateJoinNoColsRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

EliminateJoinNoColsRight eliminates an InnerJoin with a one row, zero column
right input set. These can be produced when a Values, scalar GroupBy, or other
one-row operator's columns are never used.

Extracted from `join.opt` (which defines multiple rules — implement specifically `EliminateJoinNoColsRight`, not the other rules in that file):

```
# EliminateJoinNoColsRight eliminates an InnerJoin with a one row, zero column
# right input set. These can be produced when a Values, scalar GroupBy, or other
# one-row operator's columns are never used.
[EliminateJoinNoColsRight, Normalize]
(InnerJoin | InnerJoinApply
    $left:*
    $right:* &
        (ColsAreEmpty (OutputCols $right)) &
        (HasOneRow $right)
    $on:*
)
=>
(Select $left $on)
```
