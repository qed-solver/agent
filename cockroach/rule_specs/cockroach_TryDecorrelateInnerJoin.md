# Name: TryDecorrelateInnerJoin
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateInnerJoin tries to decorrelate an InnerJoin operator nested
beneath another Join operator by pulling up its join condition to the outer
join. This may be enough to decorrelate the outer join, or it may allow any
outer column references to continue to journey upwards.

TODO(andyk): Consider adding case for outer cols in $left.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateInnerJoin`, not the other rules in that file):

```
# TryDecorrelateInnerJoin tries to decorrelate an InnerJoin operator nested
# beneath another Join operator by pulling up its join condition to the outer
# join. This may be enough to decorrelate the outer join, or it may allow any
# outer column references to continue to journey upwards.
#
# TODO(andyk): Consider adding case for outer cols in $left.
[TryDecorrelateInnerJoin, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply | SemiJoin
        | SemiJoinApply | AntiJoin | AntiJoinApply
    $left:*
    $right:* &
        (HasOuterCols $right) &
        (InnerJoin | InnerJoinApply
            $innerLeft:*
            $innerRight:*
            $innerOn:* &
                ^(FiltersBoundBy
                    $innerOn
                    (OutputCols2 $innerLeft $innerRight)
                )
            $innerPrivate:*
        )
    $on:*
    $private:*
)
=>
((OpName)
    $left
    ((OpName $right) $innerLeft $innerRight [] $innerPrivate)
    (ConcatFilters $on $innerOn)
    $private
)
```
