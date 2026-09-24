# Name: PushFilterIntoJoinLeftAndRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

PushFilterIntoJoinLeftAndRight pushes a filter into both the left and right
sides of an InnerJoin or SemiJoin if it can be mapped to use the columns of
both sides. For example, consider this query:

SELECT * FROM a JOIN b ON a.x=b.x AND a.y=b.y AND a.x + b.y < 5

In this case, we can map a.x + b.y < 5 to both sides based on the equality
filters a.x=b.x AND a.y=b.y. For the left side, we can map it to
a.x + a.y < 5, and for the right side, we can map it to b.x + b.y < 5.
Given this mapping, we can safely push the filter down to both sides and
remove it from the ON filters list.

Note that this rule is only applied when the left and right inputs do not have
outer columns. If they do, then this rule can cause undetectable cycles with
TryDecorrelateSelect, since the filter is pushed down to both sides, but then
only pulled up from the right side by TryDecorrelateSelect. For this reason,
the rule also does not apply to InnerJoinApply or SemiJoinApply.

NOTE: It is important that this rule is first among the join filter push-down
rules.

Extracted from `join.opt` (which defines multiple rules — implement specifically `PushFilterIntoJoinLeftAndRight`, not the other rules in that file):

```
# PushFilterIntoJoinLeftAndRight pushes a filter into both the left and right
# sides of an InnerJoin or SemiJoin if it can be mapped to use the columns of
# both sides. For example, consider this query:
#
#   SELECT * FROM a JOIN b ON a.x=b.x AND a.y=b.y AND a.x + b.y < 5
#
# In this case, we can map a.x + b.y < 5 to both sides based on the equality
# filters a.x=b.x AND a.y=b.y. For the left side, we can map it to
# a.x + a.y < 5, and for the right side, we can map it to b.x + b.y < 5.
# Given this mapping, we can safely push the filter down to both sides and
# remove it from the ON filters list.
#
# Note that this rule is only applied when the left and right inputs do not have
# outer columns. If they do, then this rule can cause undetectable cycles with
# TryDecorrelateSelect, since the filter is pushed down to both sides, but then
# only pulled up from the right side by TryDecorrelateSelect. For this reason,
# the rule also does not apply to InnerJoinApply or SemiJoinApply.
#
# NOTE: It is important that this rule is first among the join filter push-down
#       rules.
[PushFilterIntoJoinLeftAndRight, Normalize]
(InnerJoin | SemiJoin
    $left:* & ^(HasOuterCols $left)
    $right:* & ^(HasOuterCols $right)
    $on:[
        ...
        $item:* &
            ^(FiltersItem (Eq (Variable) (Variable))) &
            (CanMapJoinOpFilter
                $item
                $leftCols:(OutputCols $left)
                $equivSet:(GetEquivGroups $on $left $right)
            ) &
            (CanMapJoinOpFilter
                $item
                $rightCols:(OutputCols $right)
                $equivSet
            )
        ...
    ]
    $private:*
)
=>
((OpName)
    (Select
        $left
        [
            (FiltersItem
                (MapJoinOpFilter $item $leftCols $equivSet)
            )
        ]
    )
    (Select
        $right
        [
            (FiltersItem
                (MapJoinOpFilter $item $rightCols $equivSet)
            )
        ]
    )
    (RemoveFiltersItem $on $item)
    $private
)
```
