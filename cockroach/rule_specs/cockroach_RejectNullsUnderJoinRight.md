# Name: RejectNullsUnderJoinRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/reject_nulls.opt

RejectNullsUnderJoinRight mirrors RejectNullsUnderJoinLeft.

Extracted from `reject_nulls.opt` (which defines multiple rules — implement specifically `RejectNullsUnderJoinRight`, not the other rules in that file):

```
# RejectNullsUnderJoinRight mirrors RejectNullsUnderJoinLeft.
[RejectNullsUnderJoinRight, Normalize, LowPriority]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply | SemiJoin
        | AntiJoin
    $left:*
    $right:* &
        ^(ColsAreEmpty $rejectCols:(RejectNullCols $right))
    $on:* &
        ^(ColsAreEmpty
            $nullRejectedCols:(IntersectionCols
                $rejectCols
                (GetNullRejectedCols $on)
            )
        )
    $private:*
)
=>
((OpName)
    $left
    (Select $right (MakeNullRejectFilters $nullRejectedCols))
    $on
    $private
)
```
