# Name: SimplifyOffsetOrdering
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/ordering.opt

SimplifyOffsetOrdering removes redundant columns from the Offset operator's
input ordering.

Extracted from `ordering.opt` (which defines multiple rules — implement specifically `SimplifyOffsetOrdering`, not the other rules in that file):

```
# SimplifyOffsetOrdering removes redundant columns from the Offset operator's
# input ordering.
[SimplifyOffsetOrdering, Normalize]
(Offset
    $input:*
    $offset:*
    $ordering:* &
        (CanSimplifyLimitOffsetOrdering $input $ordering)
)
=>
(Offset
    $input
    $offset
    (SimplifyLimitOffsetOrdering $input $ordering)
)
```
