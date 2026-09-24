# Name: SortFiltersInJoin
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

SortFiltersInJoin ensures that any filters in an inner join are canonicalized
by sorting them.

Extracted from `join.opt` (which defines multiple rules — implement specifically `SortFiltersInJoin`, not the other rules in that file):

```
# SortFiltersInJoin ensures that any filters in an inner join are canonicalized
# by sorting them.
[SortFiltersInJoin, Normalize]
(InnerJoin
    $left:*
    $right:*
    $on:* & ^(AreFiltersSorted $on)
    $private:*
)
=>
(InnerJoin $left $right (SortFilters $on) $private)
```
