# Name: CommuteVar
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

CommuteVar ensures that variable references are on the left side of
commutative comparison and binary operators. Other patterns don't need to
handle both combinations.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `CommuteVar`, not the other rules in that file):

```
# CommuteVar ensures that variable references are on the left side of
# commutative comparison and binary operators. Other patterns don't need to
# handle both combinations.
[CommuteVar, Normalize]
(Eq | Ne | Is | IsNot | Plus | Mult | Bitand | Bitor | Bitxor
        | VectorDistance | VectorCosDistance
        | VectorNegInnerProduct
    $left:^(Variable)
    $right:(Variable)
)
=>
((OpName) $right $left)
```
