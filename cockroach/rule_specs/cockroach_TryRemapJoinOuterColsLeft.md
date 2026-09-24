# Name: TryRemapJoinOuterColsLeft
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryRemapJoinOuterColsLeft is similar to TryRemapJoinOuterColsRight, but it
applies to the left input of a join.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryRemapJoinOuterColsLeft`, not the other rules in that file):

```
# TryRemapJoinOuterColsLeft is similar to TryRemapJoinOuterColsRight, but it
# applies to the left input of a join.
[TryRemapJoinOuterColsLeft, Normalize]
(InnerJoin | InnerJoinApply | SemiJoin | SemiJoinApply
    $left:* & (HasOuterCols $left)
    $right:*
    $on:* &
        (CanMaybeRemapOuterCols $left $on) &
        (Let ($remapped $ok):(TryRemapOuterCols $left $on) $ok)
    $private:*
)
=>
((OpName) $remapped $right $on $private)
```
