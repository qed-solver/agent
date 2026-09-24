# Name: MapFilterIntoJoinRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

MapFilterIntoJoinRight is symmetric with MapFilterIntoJoinLeft. It maps
Join filter conditions to use columns from the right side of the join rather
than the left side. See that rule's comments for more details.

Extracted from `join.opt` (which defines multiple rules — implement specifically `MapFilterIntoJoinRight`, not the other rules in that file):

```
# MapFilterIntoJoinRight is symmetric with MapFilterIntoJoinLeft. It maps
# Join filter conditions to use columns from the right side of the join rather
# than the left side. See that rule's comments for more details.
[MapFilterIntoJoinRight, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply | SemiJoin
        | SemiJoinApply | AntiJoin | AntiJoinApply
    $left:*
    $right:* & ^(HasOuterCols $right)
    $on:[
        ...
        $item:* &
            ^(FiltersItem (Eq (Variable) (Variable))) &
            ^(IsBoundBy $item $rightCols:(OutputCols $right)) &
            (CanMapJoinOpFilter
                $item
                $rightCols
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
        (MapJoinOpFilter $item $rightCols $equivSet)
    )
    $private
)
```
