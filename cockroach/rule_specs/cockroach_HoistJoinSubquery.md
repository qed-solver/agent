# Name: HoistJoinSubquery
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistJoinSubquery extracts subqueries from a join filter and joins them with
the join's right input. This and other subquery hoisting patterns create a
single, top-level relational query with no nesting. This rule only applies to
join types which have a legal apply variant.

This rule is marked as low priority for the same reason as HoistSelectExists.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistJoinSubquery`, not the other rules in that file):

```
# HoistJoinSubquery extracts subqueries from a join filter and joins them with
# the join's right input. This and other subquery hoisting patterns create a
# single, top-level relational query with no nesting. This rule only applies to
# join types which have a legal apply variant.
#
# This rule is marked as low priority for the same reason as HoistSelectExists.
[HoistJoinSubquery, Normalize, LowPriority]
(InnerJoin | LeftJoin | SemiJoin | AntiJoin
    $left:*
    $right:*
    $on:[ ... $item:* & (HasHoistableSubquery $item) ... ]
    $private:*
)
=>
(HoistJoinSubquery (OpName) $left $right $on $private)
```
