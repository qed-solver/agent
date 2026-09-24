# Name: PushSelectIntoJoinLeft
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/select.opt

PushSelectIntoJoinLeft pushes Select filter conditions into the left side of
an input Join. This is possible in the case of InnerJoin, LeftJoin, SemiJoin,
and AntiJoin, as long as the condition has no dependencies on the right side
of the join. Right and Full joins are not eligible, since attempting to filter
left rows would just result in NULL left rows instead.

-- No row is returned for a.x=1, a.y=2, b.x=1, since the WHERE excludes it.
SELECT * FROM a RIGHT JOIN b ON a.x=b.x WHERE a.y < 0

-- But if the filter is incorrectly pushed down in RIGHT/FULL JOIN case,
-- then a row containing null values on the left side is returned.
SELECT * FROM (SELECT * FROM a WHERE a.y < 0) a RIGHT JOIN b ON a.x=b.x

Citations: [1]

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushSelectIntoJoinLeft`, not the other rules in that file):

```
# PushSelectIntoJoinLeft pushes Select filter conditions into the left side of
# an input Join. This is possible in the case of InnerJoin, LeftJoin, SemiJoin,
# and AntiJoin, as long as the condition has no dependencies on the right side
# of the join. Right and Full joins are not eligible, since attempting to filter
# left rows would just result in NULL left rows instead.
#
#   -- No row is returned for a.x=1, a.y=2, b.x=1, since the WHERE excludes it.
#   SELECT * FROM a RIGHT JOIN b ON a.x=b.x WHERE a.y < 0
#
#   -- But if the filter is incorrectly pushed down in RIGHT/FULL JOIN case,
#   -- then a row containing null values on the left side is returned.
#   SELECT * FROM (SELECT * FROM a WHERE a.y < 0) a RIGHT JOIN b ON a.x=b.x
#
# Citations: [1]
[PushSelectIntoJoinLeft, Normalize]
(Select
    $input:(LeftJoin | LeftJoinApply | SemiJoin | SemiJoinApply
            | AntiJoin | AntiJoinApply
        $left:*
        $right:*
        $on:*
        $private:*
    )
    $filters:[
        ...
        $item:* & (IsBoundBy $item $leftCols:(OutputCols $left))
        ...
    ]
)
=>
(Select
    ((OpName $input)
        (Select
            $left
            (ExtractBoundConditions $filters $leftCols)
        )
        $right
        $on
        $private
    )
    (ExtractUnboundConditions $filters $leftCols)
)
```
