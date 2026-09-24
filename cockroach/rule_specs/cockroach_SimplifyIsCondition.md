# Name: SimplifyIsCondition
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/select.opt

SimplifyIsCondition replaces x IS NOT DISTINCT FROM y with x = y. This
transformation is only valid if all of the following are true:

1. The expression is in the context of filtering where NULL is falsy.
2. One of x or y is non-nullable. This is required because while the
expression NULL IS NOT DISTINCT FROM NULL is true, NULL=NULL is NULL
(falsy).
3. Neither x nor y is a tuple. Tuples with NULLs have all sorts of
complicated edge cases, so we avoid them entirely. See #48299.

We conservatively also require the types of x and y to be identical. It may be
possible to lift this restriction if we can prove that it is not necessary.

Extracted from `select.opt` (which defines multiple rules — implement specifically `SimplifyIsCondition`, not the other rules in that file):

```
# SimplifyIsCondition replaces x IS NOT DISTINCT FROM y with x = y. This
# transformation is only valid if all of the following are true:
#
#   1. The expression is in the context of filtering where NULL is falsy.
#   2. One of x or y is non-nullable. This is required because while the
#      expression NULL IS NOT DISTINCT FROM NULL is true, NULL=NULL is NULL
#      (falsy).
#   3. Neither x nor y is a tuple. Tuples with NULLs have all sorts of
#      complicated edge cases, so we avoid them entirely. See #48299.
#
# We conservatively also require the types of x and y to be identical. It may be
# possible to lift this restriction if we can prove that it is not necessary.
[SimplifyIsCondition, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Is
                $left:* & ^(IsTuple $left)
                $right:* &
                    ^(IsTuple $right) &
                    (IdenticalTypes
                        (TypeOf $left)
                        (TypeOf $right)
                    ) &
                    (EitherExprIsNeverNull
                        $left
                        $right
                        (NotNullCols $input)
                    )
            )
        )
        ...
    ]
)
=>
(Select
    $input
    (ReplaceFiltersItem $filters $item (Eq $left $right))
)
```
