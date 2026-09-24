# Name: ExtractJoinComparisons
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

ExtractJoinComparisons finds equality and inequality conditions such that
one side only depends on left columns and the other only on right columns
and pushes the expressions down into Project operators. The result is a
join that has an equality or inequality constraint, which is much more
efficient. For example:

SELECT * FROM abc JOIN xyz ON a=x+1

This join would be quadratic because we have no equality columns.
This rule rewrites it as:

SELECT a,b,c,x,y,z FROM abc JOIN (SELECT *, x+1 AS x1 FROM xyz) ON a=x1

This join can use hash join or lookup on the equality columns.

Depending on the expressions involved, one or both sides require a projection.

Extracted from `join.opt` (which defines multiple rules — implement specifically `ExtractJoinComparisons`, not the other rules in that file):

```
# ExtractJoinComparisons finds equality and inequality conditions such that
# one side only depends on left columns and the other only on right columns
# and pushes the expressions down into Project operators. The result is a
# join that has an equality or inequality constraint, which is much more
# efficient. For example:
#
#   SELECT * FROM abc JOIN xyz ON a=x+1
#
# This join would be quadratic because we have no equality columns.
# This rule rewrites it as:
#
#   SELECT a,b,c,x,y,z FROM abc JOIN (SELECT *, x+1 AS x1 FROM xyz) ON a=x1
#
# This join can use hash join or lookup on the equality columns.
#
# Depending on the expressions involved, one or both sides require a projection.
[ExtractJoinComparisons, Normalize]
(JoinNonApply
    $left:* & ^(HasOuterCols $left)
    $right:* & ^(HasOuterCols $right)
    $on:[
        ...
        $item:(FiltersItem
            (Eq | Lt | Le | Gt | Ge
                    $a:^(ConstValue)
                    $b:^(ConstValue)
                ) &
                (CanExtractJoinComparison
                    $a
                    $b
                    (OutputCols $left)
                    (OutputCols $right)
                )
        )
        ...
    ]
    $private:*
)
=>
(ExtractJoinComparison (OpName) $left $right $on $item $private)
```
