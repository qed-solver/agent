# Name: NormalizeInConst
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

NormalizeInConst ensures that the In operator's tuple operand is sorted with
duplicates removed (since duplicates do not change the result).

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `NormalizeInConst`, not the other rules in that file):

```
# NormalizeInConst ensures that the In operator's tuple operand is sorted with
# duplicates removed (since duplicates do not change the result).
[NormalizeInConst, Normalize]
(In | NotIn
    $left:*
    $right:(Tuple $elems:*) & (NeedSortedUniqueList $elems)
)
=>
((OpName) $left (Tuple (ConstructSortedUniqueList $elems)))
```
