# Name: RejectNullsUnderJoinLeft
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/reject_nulls.opt

RejectNullsUnderJoinLeft adds "col IS NOT NULL" null-rejecting filters to the
left input of a join for each column that is both in the NullRejectCols ColSet
and is null-rejected by the join's filters. Note that a left join cannot be
matched even if its filters reject nulls on a column because left joins add
back unmatched columns to the output. RejectNullsUnderJoinLeft is low priority
to allow filters to be pushed down entirely, if possible.

Extracted from `reject_nulls.opt` (which defines multiple rules — implement specifically `RejectNullsUnderJoinLeft`, not the other rules in that file):

```
# RejectNullsUnderJoinLeft adds "col IS NOT NULL" null-rejecting filters to the
# left input of a join for each column that is both in the NullRejectCols ColSet
# and is null-rejected by the join's filters. Note that a left join cannot be
# matched even if its filters reject nulls on a column because left joins add
# back unmatched columns to the output. RejectNullsUnderJoinLeft is low priority
# to allow filters to be pushed down entirely, if possible.
[RejectNullsUnderJoinLeft, Normalize, LowPriority]
(InnerJoin | InnerJoinApply | SemiJoin | SemiJoinApply
    $left:* & ^(ColsAreEmpty $rejectCols:(RejectNullCols $left))
    $right:*
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
    (Select $left (MakeNullRejectFilters $nullRejectedCols))
    $right
    $on
    $private
)
```
