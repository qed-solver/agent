# Name: CommuteRightJoin
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

CommuteRightJoin converts a RightJoin to a LeftJoin with the left and right
inputs swapped. This allows other normalization rules to only worry about the
LeftJoin case.

Extracted from `join.opt` (which defines multiple rules — implement specifically `CommuteRightJoin`, not the other rules in that file):

```
# CommuteRightJoin converts a RightJoin to a LeftJoin with the left and right
# inputs swapped. This allows other normalization rules to only worry about the
# LeftJoin case.
[CommuteRightJoin, Normalize, HighPriority]
(RightJoin $left:* $right:* $on:* $private:*)
=>
(LeftJoin $right $left $on (CommuteJoinFlags $private))
```
