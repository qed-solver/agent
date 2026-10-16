# HoistProjectSubquery

**Status:** SKIPPED
**Source backend:** CockroachDB

## Source rule (as given to the porter)

```
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
```

## Reason

In the DSL a scalar subquery reaches the prover as an opaque $SCALAR_QUERY operator with no relation to a join, so a subquery in a filter or projection cannot be shown equal to the same value taken from a joined column. Encodings with a COUNT scalar subquery in a Select filter against a Project over a left join, and against an inner join, both come back unprovable. The same hoisting applies to a Project's projection list, and the rewrite is a Go function that builds apply joins, so there is no declarative form to encode.
