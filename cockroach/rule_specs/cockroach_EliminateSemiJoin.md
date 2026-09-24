# Name: EliminateSemiJoin
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

EliminateSemiJoin discards a SemiJoin when it's known that all left rows will
be matched by the join filters.

Extracted from `join.opt` (which defines multiple rules — implement specifically `EliminateSemiJoin`, not the other rules in that file):

```
# EliminateSemiJoin discards a SemiJoin when it's known that all left rows will
# be matched by the join filters.
[EliminateSemiJoin, Normalize]
(SemiJoin | SemiJoinApply
    $left:*
    $right:*
    $on:* & (JoinFiltersMatchAllLeftRows $left $right $on)
)
=>
$left
```
