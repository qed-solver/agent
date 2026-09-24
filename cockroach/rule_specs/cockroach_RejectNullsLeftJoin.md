# Name: RejectNullsLeftJoin
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/reject_nulls.opt

RejectNullsLeftJoin reduces a LeftJoin operator to an InnerJoin operator (or a
FullJoin to a RightJoin) when there is a null-rejecting filter on any column
from the right side. The effect of the null-rejecting filter is that output
rows with all NULL values on the right side created by the left (or full) join
are eliminated, making the join equivalent to an inner (or right) join. For
example:

SELECT * FROM a LEFT OUTER JOIN b ON a.x = b.x WHERE b.y < 5

can be reduced to:

SELECT * FROM a INNER JOIN b ON a.x = b.x WHERE b.y < 5

since b.y < 5 is a null-rejecting filter on the right side.

This rule is marked as high priority so that it runs before Select filter
pushdown rules. Those rules may remove a filter before it's had a chance to
rewrite the input join.

Citations: [1]

Extracted from `reject_nulls.opt` (which defines multiple rules — implement specifically `RejectNullsLeftJoin`, not the other rules in that file):

```
# RejectNullsLeftJoin reduces a LeftJoin operator to an InnerJoin operator (or a
# FullJoin to a RightJoin) when there is a null-rejecting filter on any column
# from the right side. The effect of the null-rejecting filter is that output
# rows with all NULL values on the right side created by the left (or full) join
# are eliminated, making the join equivalent to an inner (or right) join. For
# example:
#
#   SELECT * FROM a LEFT OUTER JOIN b ON a.x = b.x WHERE b.y < 5
#
# can be reduced to:
#
#   SELECT * FROM a INNER JOIN b ON a.x = b.x WHERE b.y < 5
#
# since b.y < 5 is a null-rejecting filter on the right side.
#
# This rule is marked as high priority so that it runs before Select filter
# pushdown rules. Those rules may remove a filter before it's had a chance to
# rewrite the input join.
#
# Citations: [1]
[RejectNullsLeftJoin, Normalize, HighPriority]
(Select
    $input:(LeftJoin | LeftJoinApply | FullJoin
        $left:*
        $right:*
        $on:*
        $private:*
    )
    $filters:* &
        (HasNullRejectingFilter $filters (OutputCols $right))
)
=>
(Select
    (ConstructNonLeftJoin
        (OpName $input)
        $left
        $right
        $on
        $private
    )
    $filters
)
```
