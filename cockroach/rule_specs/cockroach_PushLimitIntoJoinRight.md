# Name: PushLimitIntoJoinRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/limit.opt

PushLimitIntoJoinRight mirrors PushLimitIntoJoinLeft.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `PushLimitIntoJoinRight`, not the other rules in that file):

```
# PushLimitIntoJoinRight mirrors PushLimitIntoJoinLeft.
[PushLimitIntoJoinRight, Normalize]
(Limit
    $input:(InnerJoin
            $left:*
            $right:* & ^(HasOuterCols $right)
            $on:*
            $private:*
        ) &
        (JoinPreservesRightRows $input)
    $limitExpr:(Const $limit:*) &
        (IsPositiveInt $limit) &
        (CanRepresentMaxRows $limit) &
        ^(LimitGeMaxRows $limit $right)
    $ordering:* &
        (OrderingCanProjectCols
            $ordering
            $cols:(OutputCols $right)
        )
)
=>
(Limit
    ((OpName $input)
        $left
        (Limit $right $limitExpr (PruneOrdering $ordering $cols))
        $on
        $private
    )
    $limitExpr
    $ordering
)
```
