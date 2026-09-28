# NormalizeArrayFlattenToAgg

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 25  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

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
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests entirely on semantics QED's model lacks: array_agg returning NULL over an empty input (aggregate algebra beyond bag equality of its input), COALESCE's "first non-NULL" behavior, and array/list value semantics (an array is a list whose contents and, for ordered subqueries, ordering matter — list/ordering semantics QED explicitly does not model). Even extending the DSL with ArrayFlatten/Coalesce/scalar-subquery builders would only let the rule be *stated*: the left side's flatten and the right side's coalesce∘array_agg would serialize as independent uninterpreted symbols over an opaque array value type the SMT solver cannot decompose or relate, so no instantiation-independent proof — including the non-empty-input special case — is possible. ```
