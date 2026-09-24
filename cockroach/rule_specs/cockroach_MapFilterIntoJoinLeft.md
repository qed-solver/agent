# Name: MapFilterIntoJoinLeft
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

MapFilterIntoJoinLeft maps a filter that is not bound by the left side of
the join to use the columns from the left side. This will allow
the filter to be pushed down by the PushFilterIntoJoinLeft rule.
For example, consider this query:

SELECT * FROM a INNER JOIN b ON a.x = b.x AND b.x + a.y < 5

In this case, we can map b.x + a.y < 5 to the left side by replacing b.x
with the equivalent column a.x.
NOTE: This rule only applies to cases where it is not possible or not safe
to map the filter to both sides. If it can be mapped to both sides, it
will be handled by PushFilterIntoJoinLeftAndRight (which must be
ordered above this rule). For performance reasons, this rule should
be ordered before PushFilterIntoJoinLeft (otherwise,
PushFilterIntoJoinLeft might need to be applied multiple times).

Extracted from `join.opt` (which defines multiple rules — implement specifically `MapFilterIntoJoinLeft`, not the other rules in that file):

```
# MapFilterIntoJoinLeft maps a filter that is not bound by the left side of
# the join to use the columns from the left side. This will allow
# the filter to be pushed down by the PushFilterIntoJoinLeft rule.
# For example, consider this query:
#
#   SELECT * FROM a INNER JOIN b ON a.x = b.x AND b.x + a.y < 5
#
# In this case, we can map b.x + a.y < 5 to the left side by replacing b.x
# with the equivalent column a.x.
# NOTE: This rule only applies to cases where it is not possible or not safe
#       to map the filter to both sides. If it can be mapped to both sides, it
#       will be handled by PushFilterIntoJoinLeftAndRight (which must be
#       ordered above this rule). For performance reasons, this rule should
#       be ordered before PushFilterIntoJoinLeft (otherwise,
#       PushFilterIntoJoinLeft might need to be applied multiple times).
[MapFilterIntoJoinLeft, Normalize]
(InnerJoin | InnerJoinApply | SemiJoin | SemiJoinApply
    $left:* & ^(HasOuterCols $left)
    $right:*
    $on:[
        ...
        $item:* &
            ^(FiltersItem (Eq (Variable) (Variable))) &
            ^(IsBoundBy $item $leftCols:(OutputCols $left)) &
            (CanMapJoinOpFilter
                $item
                $leftCols
                $equivSet:(GetEquivGroups $on $left $right)
            )
        ...
    ]
    $private:*
)
=>
((OpName)
    $left
    $right
    (ReplaceFiltersItem
        $on
        $item
        (MapJoinOpFilter $item $leftCols $equivSet)
    )
    $private
)
```
