# Name: SimplifyOrdinalityOrdering
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/ordering.opt

SimplifyOrdinalityOrdering removes redundant columns from the Ordinality
operator's input ordering.

Extracted from `ordering.opt` (which defines multiple rules — implement specifically `SimplifyOrdinalityOrdering`, not the other rules in that file):

```
# SimplifyOrdinalityOrdering removes redundant columns from the Ordinality
# operator's input ordering.
[SimplifyOrdinalityOrdering, Normalize]
(Ordinality
    $input:*
    $ordinalityPrivate:* &
        (CanSimplifyOrdinalityOrdering $input $ordinalityPrivate)
)
=>
(Ordinality
    $input
    (SimplifyOrdinalityOrdering $input $ordinalityPrivate)
)
```
