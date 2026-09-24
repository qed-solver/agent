# Name: PushSelectCondLeftIntoJoinLeftAndRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/select.opt

PushSelectCondLeftIntoJoinLeftAndRight applies to the case when a condition
bound by the left side of a join can be mapped to the right side using
equality columns from the ON condition of the join. It pushes the original
filter to the left side, and the mapped filter to the right side.
For example, consider this query:

SELECT * FROM l LEFT JOIN r ON l.x = r.x WHERE l.x = 5;

This can safely be converted to:

SELECT * FROM (SELECT * FROM l WHERE l.x = 5)
LEFT JOIN (SELECT * FROM r WHERE r.x = 5) ON l.x = r.x;

It's not normally correct to push filters from the SELECT clause to
the right side of a LEFT JOIN, since those rows might still show up
in the output as NULL-extended rows from the left side. In this case,
however, for any rows removed from the right side, the matching rows are
also removed from the left side (and thus removed from the output).
To ensure that this is the case, it's important that the filter only refers
to columns on the left side that have corresponding equivalent columns on
the right side.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushSelectCondLeftIntoJoinLeftAndRight`, not the other rules in that file):

```
# PushSelectCondLeftIntoJoinLeftAndRight applies to the case when a condition
# bound by the left side of a join can be mapped to the right side using
# equality columns from the ON condition of the join. It pushes the original
# filter to the left side, and the mapped filter to the right side.
# For example, consider this query:
#
#   SELECT * FROM l LEFT JOIN r ON l.x = r.x WHERE l.x = 5;
#
# This can safely be converted to:
#
#   SELECT * FROM (SELECT * FROM l WHERE l.x = 5)
#   LEFT JOIN (SELECT * FROM r WHERE r.x = 5) ON l.x = r.x;
#
# It's not normally correct to push filters from the SELECT clause to
# the right side of a LEFT JOIN, since those rows might still show up
# in the output as NULL-extended rows from the left side. In this case,
# however, for any rows removed from the right side, the matching rows are
# also removed from the left side (and thus removed from the output).
# To ensure that this is the case, it's important that the filter only refers
# to columns on the left side that have corresponding equivalent columns on
# the right side.
[PushSelectCondLeftIntoJoinLeftAndRight, Normalize]
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
        $item:(FiltersItem $condition:*) &
            (IsBoundBy $item (OutputCols $left)) &
            (CanMapJoinOpFilter
                $item
                $rightCols:(OutputCols $right)
                $equivSet:(GetEquivGroups $on $left $right)
            )
        ...
    ]
)
=>
(Select
    ((OpName $input)
        (Select $left [ (FiltersItem $condition) ])
        (Select
            $right
            [
                (FiltersItem
                    (MapJoinOpFilter $item $rightCols $equivSet)
                )
            ]
        )
        $on
        $private
    )
    (RemoveFiltersItem $filters $item)
)
```
