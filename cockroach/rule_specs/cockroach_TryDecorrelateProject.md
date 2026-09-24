# Name: TryDecorrelateProject
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateProject "pushes down" a Join into a Project operator, in an
attempt to eliminate any correlation between the projection list and the left
side of the join, and also to keep "digging" down to find and eliminate other
unnecessary correlation. The eventual hope is to trigger the DecorrelateJoin
rule to turn a JoinApply operator into a non-apply Join operator.

Citations: [3] (see identity #4)

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateProject`, not the other rules in that file):

```
# TryDecorrelateProject "pushes down" a Join into a Project operator, in an
# attempt to eliminate any correlation between the projection list and the left
# side of the join, and also to keep "digging" down to find and eliminate other
# unnecessary correlation. The eventual hope is to trigger the DecorrelateJoin
# rule to turn a JoinApply operator into a non-apply Join operator.
#
# Citations: [3] (see identity #4)
[TryDecorrelateProject, Normalize]
(InnerJoin | InnerJoinApply
    $left:*
    $right:* &
        (HasOuterCols $right) &
        (Project $input:* $projections:* $passthrough:*)
    $on:*
    $private:*
)
=>
(Select
    (Project
        ((OpName) $left $input [] $private)
        $projections
        (UnionCols (OutputCols $left) $passthrough)
    )
    $on
)
```
