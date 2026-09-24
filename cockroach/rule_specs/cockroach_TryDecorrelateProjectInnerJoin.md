# Name: TryDecorrelateProjectInnerJoin
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateProjectInnerJoin tries to decorrelate by hoisting the filter of
an InnerJoin operator that sits below a LeftJoin/Project operator combo. The
Project operator itself can't be reordered above the LeftJoin like it can in
the InnerJoin case. However, the InnerJoin filter can be merged with the
LeftJoin filter, which is enough to decorrelate in several useful cases. This
rule works similarly to TryDecorrelateProjectSelect.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateProjectInnerJoin`, not the other rules in that file):

```
# TryDecorrelateProjectInnerJoin tries to decorrelate by hoisting the filter of
# an InnerJoin operator that sits below a LeftJoin/Project operator combo. The
# Project operator itself can't be reordered above the LeftJoin like it can in
# the InnerJoin case. However, the InnerJoin filter can be merged with the
# LeftJoin filter, which is enough to decorrelate in several useful cases. This
# rule works similarly to TryDecorrelateProjectSelect.
[TryDecorrelateProjectInnerJoin, Normalize, HighPriority]
(LeftJoinApply
    $left:*
    $right:(Project
        $join:(InnerJoin | InnerJoinApply
            $innerLeft:*
            $innerRight:*
            $innerOn:* &
                ^(FiltersBoundBy
                    $innerOn
                    (OutputCols2 $innerLeft $innerRight)
                )
            $innerPrivate:*
        )
        $projections:*
        $passthrough:*
    )
    $on:*
    $private:*
)
=>
(Project
    (LeftJoinApply
        $left
        (Project
            ((OpName $join)
                $innerLeft
                $innerRight
                []
                $innerPrivate
            )
            $projections
            (UnionCols $passthrough (OutputCols $join))
        )
        (ConcatFilters $on $innerOn)
        $private
    )
    []
    (OutputCols2 $left $right)
)
```
