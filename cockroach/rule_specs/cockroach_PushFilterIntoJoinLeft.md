# Name: PushFilterIntoJoinLeft
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

PushFilterIntoJoinLeft pushes Join filter conditions into the left side of the
join. This is possible in the case of InnerJoin, as long as the condition has
no dependencies on the right side of the join. Left and Full joins are not
eligible, since filtering left rows will change the number of rows in the
result for those types of joins:

-- A row with nulls on the right side is returned for a.x=1, a.y=2, b.x=1.
SELECT * FROM a LEFT JOIN b ON a.x=b.x AND a.y < 0

-- But if the filter is incorrectly pushed down, then no row is returned.
SELECT * FROM (SELECT * FROM a WHERE a.y < 0) a LEFT JOIN b ON a.x=b.x

In addition, AntiJoin is not eligible for this rule, as illustrated by this
example:

-- A row is returned for a.y=2.
SELECT * FROM a ANTI JOIN b ON a.y < 0

-- But if the filter is incorrectly pushed down, then no row is returned.
SELECT * FROM (SELECT * FROM a WHERE a.y < 0) a ANTI JOIN b ON True

Citations: [1]

Extracted from `join.opt` (which defines multiple rules — implement specifically `PushFilterIntoJoinLeft`, not the other rules in that file):

```
# PushFilterIntoJoinLeft pushes Join filter conditions into the left side of the
# join. This is possible in the case of InnerJoin, as long as the condition has
# no dependencies on the right side of the join. Left and Full joins are not
# eligible, since filtering left rows will change the number of rows in the
# result for those types of joins:
#
#   -- A row with nulls on the right side is returned for a.x=1, a.y=2, b.x=1.
#   SELECT * FROM a LEFT JOIN b ON a.x=b.x AND a.y < 0
#
#   -- But if the filter is incorrectly pushed down, then no row is returned.
#   SELECT * FROM (SELECT * FROM a WHERE a.y < 0) a LEFT JOIN b ON a.x=b.x
#
# In addition, AntiJoin is not eligible for this rule, as illustrated by this
# example:
#
#   -- A row is returned for a.y=2.
#   SELECT * FROM a ANTI JOIN b ON a.y < 0
#
#   -- But if the filter is incorrectly pushed down, then no row is returned.
#   SELECT * FROM (SELECT * FROM a WHERE a.y < 0) a ANTI JOIN b ON True
#
# Citations: [1]
[PushFilterIntoJoinLeft, Normalize]
(InnerJoin | InnerJoinApply | SemiJoin | SemiJoinApply
    $left:* & ^(HasOuterCols $left)
    $right:*
    $on:[
        ...
        $item:* & (IsBoundBy $item $leftCols:(OutputCols $left))
        ...
    ]
    $private:*
)
=>
((OpName)
    (Select $left (ExtractBoundConditions $on $leftCols))
    $right
    (ExtractUnboundConditions $on $leftCols)
    $private
)
```
