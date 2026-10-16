# HoistProjectSetSubquery

**Status:** SKIPPED
**Source backend:** CockroachDB

## Source rule (as given to the porter)

```
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
```

## Reason

The rule operates on ProjectSet, whose zipped set-returning functions have no operator in the DSL or the prover, so even the unhoisted input cannot be written. In the DSL a scalar subquery reaches the prover as an opaque $SCALAR_QUERY operator with no relation to a join, so a subquery in a filter or projection cannot be shown equal to the same value taken from a joined column. Encodings with a COUNT scalar subquery in a Select filter against a Project over a left join, and against an inner join, both come back unprovable. The rewrite is a Go function that builds apply joins.
