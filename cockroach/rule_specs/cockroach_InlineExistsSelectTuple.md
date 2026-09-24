# Name: InlineExistsSelectTuple
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

InlineExistsSelectTuple splits a tuple equality filter into multiple
(per-column) equalities, in the case where the tuple on one side is being
projected.

We are specifically handling the case when this is under Exists because we
don't have to keep the same output columns for the Select. This case is
important because it is produced for an IN subquery:

SELECT * FROM ab WHERE (a, b) IN (SELECT c, d FROM cd)

Without this rule, we would not be able to produce a lookup join plan for such
a query.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `InlineExistsSelectTuple`, not the other rules in that file):

```
# InlineExistsSelectTuple splits a tuple equality filter into multiple
# (per-column) equalities, in the case where the tuple on one side is being
# projected.
#
# We are specifically handling the case when this is under Exists because we
# don't have to keep the same output columns for the Select. This case is
# important because it is produced for an IN subquery:
#
#   SELECT * FROM ab WHERE (a, b) IN (SELECT c, d FROM cd)
#
# Without this rule, we would not be able to produce a lookup join plan for such
# a query.
#
[InlineExistsSelectTuple, Normalize]
(Exists
    (Select
        (Project
            $input:*
            [
                ...
                (ProjectionsItem $tuple:(Tuple) $tupleCol:*)
                ...
            ]
        )
        $filters:[
            ...
            $item:(FiltersItem
                (Eq
                    # CommuteVar ensures that the variable is on the left.
                    (Variable
                        $varCol:* &
                            (EqualsColumn $varCol $tupleCol)
                    )
                    $rhs:(Tuple) &
                        (TuplesHaveSameLength $tuple $rhs)
                )
            )
            ...
        ]
    )
    $existsPrivate:*
)
=>
(Exists
    (Select
        $input
        (ConcatFilters
            (RemoveFiltersItem $filters $item)
            (SplitTupleEq $tuple $rhs)
        )
    )
    $existsPrivate
)
```
