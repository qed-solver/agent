# Name: HoistSelectNotExists
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistSelectNotExists extracts non-existential subqueries from Select filters,
turning them into anti-joins. This eliminates the subquery, which is often
expensive to execute and restricts the optimizer's plan choices.

This rule is marked as low priority for the same reason as HoistSelectExists.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistSelectNotExists`, not the other rules in that file):

```
# HoistSelectNotExists extracts non-existential subqueries from Select filters,
# turning them into anti-joins. This eliminates the subquery, which is often
# expensive to execute and restricts the optimizer's plan choices.
#
# This rule is marked as low priority for the same reason as HoistSelectExists.
[HoistSelectNotExists, Normalize, LowPriority]
(Select
    $input:*
    $filters:[
        ...
        $item:* &
            (HasHoistableSubquery $item) &
            (FiltersItem (Not (Exists $subquery:*)))
        ...
    ]
)
=>
(Select
    (AntiJoinApply $input $subquery [] (EmptyJoinPrivate))
    (RemoveFiltersItem $filters $item)
)
```
