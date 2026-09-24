# Name: NormalizeArrayFlattenToAgg
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

ArrayFlattenToAgg converts a correlated ArrayFlatten to an aggregation.
This rule exists because:

1. We cannot do the aggregation method if we don't have a scalar type
(for instance, if we have a tuple type).
2. We cannot decorrelate an ArrayFlatten directly (but we can decorrelate
an aggregation). So it's desirable to perform this conversion in the
interest of decorrelation.

So the outcome is that we can perform uncorrelated ARRAY(...)s over any
datatype, and correlated ones only over the types that array_agg supports.

Note that optbuilder should have already verified that if the input is
correlated, then we can array_agg over the input type.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `NormalizeArrayFlattenToAgg`, not the other rules in that file):

```
# ArrayFlattenToAgg converts a correlated ArrayFlatten to an aggregation.
# This rule exists because:
#
#     1. We cannot do the aggregation method if we don't have a scalar type
#        (for instance, if we have a tuple type).
#     2. We cannot decorrelate an ArrayFlatten directly (but we can decorrelate
#        an aggregation). So it's desirable to perform this conversion in the
#        interest of decorrelation.
#
# So the outcome is that we can perform uncorrelated ARRAY(...)s over any
# datatype, and correlated ones only over the types that array_agg supports.
#
# Note that optbuilder should have already verified that if the input is
# correlated, then we can array_agg over the input type.
[NormalizeArrayFlattenToAgg, Normalize]
(ArrayFlatten
    $input:*
    $private:* & (CanNormalizeArrayFlatten $input $private)
)
=>
(Coalesce
    [
        (Subquery
            (ScalarGroupBy
                $input
                [
                    (AggregationsItem
                        (ArrayAgg
                            (Variable
                                $requestedCol:(SubqueryRequestedCol
                                    $private
                                )
                            )
                        )
                        (MakeArrayAggCol
                            (ArrayType $requestedCol)
                        )
                    )
                ]
                (MakeGrouping
                    (MakeEmptyColSet)
                    (SubqueryOrdering $private)
                )
            )
            (MakeUnorderedSubquery)
        )
        (Array [] (ArrayType $requestedCol))
    ]
)
```
