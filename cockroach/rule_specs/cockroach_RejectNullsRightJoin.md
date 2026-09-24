# Name: RejectNullsRightJoin
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/reject_nulls.opt

RejectNullsRightJoin is symmetric with RejectNullsLeftJoin. It reduces a
FullJoin operator to a LeftJoin when there is a null-rejecting filter on any
column from the left side.

This rule is marked as high priority for the same reason as
RejectNullsLeftJoin.

Extracted from `reject_nulls.opt` (which defines multiple rules — implement specifically `RejectNullsRightJoin`, not the other rules in that file):

```
# RejectNullsRightJoin is symmetric with RejectNullsLeftJoin. It reduces a
# FullJoin operator to a LeftJoin when there is a null-rejecting filter on any
# column from the left side.
#
# This rule is marked as high priority for the same reason as
# RejectNullsLeftJoin.
[RejectNullsRightJoin, Normalize, HighPriority]
(Select
    $input:(FullJoin $left:* $right:* $on:* $private:*)
    $filters:* &
        (HasNullRejectingFilter $filters (OutputCols $left))
)
=>
(Select (LeftJoin $left $right $on $private) $filters)
```
