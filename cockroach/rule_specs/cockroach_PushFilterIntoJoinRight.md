# Name: PushFilterIntoJoinRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

PushFilterIntoJoinRight is symmetric with PushFilterIntoJoinLeft. It pushes
Join filter conditions into the right side of the join rather than into the
left side. See that rule's comments for more details.

Extracted from `join.opt` (which defines multiple rules — implement specifically `PushFilterIntoJoinRight`, not the other rules in that file):

```
# PushFilterIntoJoinRight is symmetric with PushFilterIntoJoinLeft. It pushes
# Join filter conditions into the right side of the join rather than into the
# left side. See that rule's comments for more details.
[PushFilterIntoJoinRight, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply | SemiJoin
        | SemiJoinApply | AntiJoin | AntiJoinApply
    $left:*
    $right:* & ^(HasOuterCols $right)
    $on:[
        ...
        $item:* &
            (IsBoundBy $item $rightCols:(OutputCols $right))
        ...
    ]
    $private:*
)
=>
((OpName)
    $left
    (Select $right (ExtractBoundConditions $on $rightCols))
    (ExtractUnboundConditions $on $rightCols)
    $private
)
```
