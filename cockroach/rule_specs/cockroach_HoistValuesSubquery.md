# Name: HoistValuesSubquery
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistValuesSubquery extracts subqueries from row tuples and joins them with
the Values operator. This and other subquery hoisting patterns create a
single, top-level relational query with no nesting.

This rule is marked as low priority for the same reason as HoistSelectExists.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistValuesSubquery`, not the other rules in that file):

```
# HoistValuesSubquery extracts subqueries from row tuples and joins them with
# the Values operator. This and other subquery hoisting patterns create a
# single, top-level relational query with no nesting.
#
# This rule is marked as low priority for the same reason as HoistSelectExists.
[HoistValuesSubquery, Normalize, LowPriority]
(Values
    $rows:[ ... $item:* & (HasHoistableSubquery $item) ... ]
    $private:*
)
=>
(HoistValuesSubquery $rows $private)
```
