# Name: SimplifyLimitOrdering
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/ordering.opt

SimplifyLimitOrdering removes redundant columns from the Limit operator's
input ordering.

Extracted from `ordering.opt` (which defines multiple rules — implement specifically `SimplifyLimitOrdering`, not the other rules in that file):

```
# SimplifyLimitOrdering removes redundant columns from the Limit operator's
# input ordering.
[SimplifyLimitOrdering, Normalize]
(Limit
    $input:*
    $limit:*
    $ordering:* &
        (CanSimplifyLimitOffsetOrdering $input $ordering)
)
=>
(Limit
    $input
    $limit
    (SimplifyLimitOffsetOrdering $input $ordering)
)
```
