# HoistSelectSubquery

**Status:** SKIPPED
**Source backend:** CockroachDB

## Source rule (as given to the porter)

```
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
```

## Reason

In the DSL a scalar subquery reaches the prover as an opaque $SCALAR_QUERY operator with no relation to a join, so a subquery in a filter or projection cannot be shown equal to the same value taken from a joined column. Encodings with a COUNT scalar subquery in a Select filter against a Project over a left join, and against an inner join, both come back unprovable. The rule is also generic: HasHoistableSubquery matches any scalar, Any or Exists subquery and the rewrite is a Go function that builds apply joins, which has no declarative form to encode.
