# Name: MapEqualityIntoJoinLeftAndRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

MapEqualityIntoJoinLeftAndRight checks whether it is possible to map
equality conditions in a join to use different variables so that the
number of conditions crossing both sides of a join are minimized. If so,
the MapEqualityConditions function performs this mapping to construct new
filters.

For example, consider this query:

SELECT * FROM a, b WHERE a.x = b.x AND b.x = a.y;

As written, both equality conditions contain variables from both sides of
the join. We can rewrite this query, however, so that only one condition
spans both sides:

SELECT * FROM a, b WHERE a.x = a.y AND b.x = a.y;

Now the condition a.x = a.y is fully bound by the left side of the join,
and is available to be pushed down by PushFilterIntoJoinLeft.

See the MapEqualityConditions function for more details.

Extracted from `join.opt` (which defines multiple rules — implement specifically `MapEqualityIntoJoinLeftAndRight`, not the other rules in that file):

```
# MapEqualityIntoJoinLeftAndRight checks whether it is possible to map
# equality conditions in a join to use different variables so that the
# number of conditions crossing both sides of a join are minimized. If so,
# the MapEqualityConditions function performs this mapping to construct new
# filters.
#
# For example, consider this query:
#
#   SELECT * FROM a, b WHERE a.x = b.x AND b.x = a.y;
#
# As written, both equality conditions contain variables from both sides of
# the join. We can rewrite this query, however, so that only one condition
# spans both sides:
#
#   SELECT * FROM a, b WHERE a.x = a.y AND b.x = a.y;
#
# Now the condition a.x = a.y is fully bound by the left side of the join,
# and is available to be pushed down by PushFilterIntoJoinLeft.
#
# See the MapEqualityConditions function for more details.
[MapEqualityIntoJoinLeftAndRight, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply | SemiJoin
        | SemiJoinApply | AntiJoin | AntiJoinApply
    $left:* & ^(HasOuterCols $left)
    $right:* & ^(HasOuterCols $right)
    $on:* &
        (CanMapJoinOpEqualities
            $on
            $leftCols:(OutputCols $left)
            $rightCols:(OutputCols $right)
        )
    $private:*
)
=>
((OpName)
    $left
    $right
    (MapJoinOpEqualities $on $leftCols $rightCols)
    $private
)
```
