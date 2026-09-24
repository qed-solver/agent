# Name: HoistSelectExists
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistSelectExists extracts existential subqueries from Select filters,
turning them into semi-joins. This eliminates the subquery, which is often
expensive to execute and restricts the optimizer's plan choices.

This rule is marked as low priority so that it runs after other rules like
filter pushdown. Hoisting a correlated subquery is an expensive operation that
can't be undone, so do it only once all other work is complete. For example,
filter pushdown rules might be able to move the subquery nearer to the input
to which it's correlated before it's hoisted, making it easier to decorrelate.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistSelectExists`, not the other rules in that file):

```
# HoistSelectExists extracts existential subqueries from Select filters,
# turning them into semi-joins. This eliminates the subquery, which is often
# expensive to execute and restricts the optimizer's plan choices.
#
# This rule is marked as low priority so that it runs after other rules like
# filter pushdown. Hoisting a correlated subquery is an expensive operation that
# can't be undone, so do it only once all other work is complete. For example,
# filter pushdown rules might be able to move the subquery nearer to the input
# to which it's correlated before it's hoisted, making it easier to decorrelate.
[HoistSelectExists, Normalize, LowPriority]
(Select
    $input:*
    $filters:[
        ...
        $item:* &
            (HasHoistableSubquery $item) &
            (FiltersItem (Exists $subquery:*))
        ...
    ]
)
=>
(Select
    (SemiJoinApply $input $subquery [] (EmptyJoinPrivate))
    (RemoveFiltersItem $filters $item)
)
```
