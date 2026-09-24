# Name: HoistSelectSubquery
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistSelectSubquery extracts subqueries from a Select filter and joins them
with the Select input. This and other subquery hoisting patterns create a
single, top-level relational query with no nesting.

NOTE: Keep this ordered after the HoistSelectExists and HoistSelectNotExists
rules. This rule will hoist any existential subqueries using
LeftJoinApply, which is equivalent to, but not as efficient as, using
SemiJoinApply and AntiJoinApply.

This rule is marked as low priority for the same reason as HoistSelectExists.

Citations: [4]

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistSelectSubquery`, not the other rules in that file):

```
# HoistSelectSubquery extracts subqueries from a Select filter and joins them
# with the Select input. This and other subquery hoisting patterns create a
# single, top-level relational query with no nesting.
#
# NOTE: Keep this ordered after the HoistSelectExists and HoistSelectNotExists
#       rules. This rule will hoist any existential subqueries using
#       LeftJoinApply, which is equivalent to, but not as efficient as, using
#       SemiJoinApply and AntiJoinApply.
#
# This rule is marked as low priority for the same reason as HoistSelectExists.
#
# Citations: [4]
[HoistSelectSubquery, Normalize, LowPriority]
(Select
    $input:*
    $filters:[ ... $item:* & (HasHoistableSubquery $item) ... ]
)
=>
(HoistSelectSubquery $input $filters)
```
