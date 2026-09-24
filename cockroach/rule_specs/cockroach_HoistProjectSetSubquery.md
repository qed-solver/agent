# Name: HoistProjectSetSubquery
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistProjectSetSubquery extracts subqueries from zipped functions and joins
them with the ProjectSet operator's input. This and other subquery hoisting
patterns create a single, top-level relational query with no nesting.

This rule is marked as low priority for the same reason as HoistSelectExists.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistProjectSetSubquery`, not the other rules in that file):

```
# HoistProjectSetSubquery extracts subqueries from zipped functions and joins
# them with the ProjectSet operator's input. This and other subquery hoisting
# patterns create a single, top-level relational query with no nesting.
#
# This rule is marked as low priority for the same reason as HoistSelectExists.
[HoistProjectSetSubquery, Normalize, LowPriority]
(ProjectSet
    $input:*
    $zip:[ ... $item:* & (HasHoistableSubquery $item) ... ]
)
=>
(HoistProjectSetSubquery $input $zip)
```
