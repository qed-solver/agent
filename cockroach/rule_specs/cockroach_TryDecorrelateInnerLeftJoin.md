# Name: TryDecorrelateInnerLeftJoin
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateInnerLeftJoin tries to decorrelate a LeftJoin operator nested
beneath an InnerJoin operator by using the associative identity to pull up the
left join to become the outer join. This may be enough to decorrelate the
outer join, or it may allow any outer column references to continue to journey
upwards.

Citations: [1] (see identity #6)

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateInnerLeftJoin`, not the other rules in that file):

```
# TryDecorrelateInnerLeftJoin tries to decorrelate a LeftJoin operator nested
# beneath an InnerJoin operator by using the associative identity to pull up the
# left join to become the outer join. This may be enough to decorrelate the
# outer join, or it may allow any outer column references to continue to journey
# upwards.
#
# Citations: [1] (see identity #6)
[TryDecorrelateInnerLeftJoin, Normalize]
(InnerJoin | InnerJoinApply
    $left:*
    $right:* &
        (HasOuterCols $right) &
        (LeftJoin
            $innerLeft:*
            $innerRight:*
            $innerOn:*
            $innerPrivate:*
        )
    $on:* & (FiltersBoundBy $on (OutputCols2 $left $innerLeft))
    $private:*
)
=>
(LeftJoinApply
    ((OpName) $left $innerLeft $on $innerPrivate)
    $innerRight
    $innerOn
    $private
)
```
