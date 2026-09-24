# Name: CommuteConst
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

CommuteConst ensures that "constant expression trees" are on the right side
of commutative comparison and binary operators. A constant expression tree
has no unbound variables that refer to outer columns. It therefore always
evaluates to the same result. Note that this is possible even if the tree
contains variable expressions, as long as they are bound, such as in
uncorrelated subqueries:

SELECT * FROM a WHERE a.x = (SELECT SUM(b.x) FROM b)

The right side of the equality expression is a constant expression tree, even
though it contains an entire subquery, because it always evaluates to the same
result. The left side is not a constant expression tree, even though it
contains just a single variable, because its value can be different for each
row in the table "a".

The goal of this and related patterns is to push constant expression trees to
the right side until only a Variable remains on the left (if possible). Other
patterns can rely on this normal form and only handle one combination.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `CommuteConst`, not the other rules in that file):

```
# CommuteConst ensures that "constant expression trees" are on the right side
# of commutative comparison and binary operators. A constant expression tree
# has no unbound variables that refer to outer columns. It therefore always
# evaluates to the same result. Note that this is possible even if the tree
# contains variable expressions, as long as they are bound, such as in
# uncorrelated subqueries:
#
#   SELECT * FROM a WHERE a.x = (SELECT SUM(b.x) FROM b)
#
# The right side of the equality expression is a constant expression tree, even
# though it contains an entire subquery, because it always evaluates to the same
# result. The left side is not a constant expression tree, even though it
# contains just a single variable, because its value can be different for each
# row in the table "a".
#
# The goal of this and related patterns is to push constant expression trees to
# the right side until only a Variable remains on the left (if possible). Other
# patterns can rely on this normal form and only handle one combination.
[CommuteConst, Normalize]
(Eq | Ne | Is | IsNot | Plus | Mult | Bitand | Bitor | Bitxor
    $left:(ConstValue)
    $right:^(ConstValue)
)
=>
((OpName) $right $left)
```
