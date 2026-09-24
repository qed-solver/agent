# Name: HoistProjectSubquery
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistProjectSubquery extracts subqueries from a projections list and joins
them with the Project input. This and other subquery hoisting patterns create
a single, top-level relational query with no nesting.

This rule is marked as low priority for the same reason as HoistSelectExists.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistProjectSubquery`, not the other rules in that file):

```
# HoistProjectSubquery extracts subqueries from a projections list and joins
# them with the Project input. This and other subquery hoisting patterns create
# a single, top-level relational query with no nesting.
#
# This rule is marked as low priority for the same reason as HoistSelectExists.
[HoistProjectSubquery, Normalize, LowPriority]
(Project
    $input:*
    $projections:[
        ...
        $item:* & (HasHoistableSubquery $item)
        ...
    ]
    $passthrough:*
)
=>
(HoistProjectSubquery $input $projections $passthrough)
```
