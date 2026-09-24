# Name: SimplifyJoinFilters
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

SimplifyJoinFilters works like SimplifySelectFilters, except that it operates
on Join filters rather than Select filters.

Extracted from `join.opt` (which defines multiple rules — implement specifically `SimplifyJoinFilters`, not the other rules in that file):

```
# SimplifyJoinFilters works like SimplifySelectFilters, except that it operates
# on Join filters rather than Select filters.
[SimplifyJoinFilters, Normalize, HighPriority]
(Join
    $left:*
    $right:*
    $on:[
            ...
            $item:(FiltersItem
                    (And | True | False | Null | Or | Is)
                ) &
                ^(IsUnsimplifiableOr $item) &
                ^(IsUnsimplifiableIs $item) &
                ^(IsContradiction $item)
            ...
        ] &
        ^(IsFilterFalse $on)
    $private:*
)
=>
((OpName) $left $right (SimplifyFilters $on) $private)
```
