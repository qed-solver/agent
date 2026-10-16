# NormalizeArrayFlattenToAgg

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 31  **Verification rounds used:** 2

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

The rule's soundness is precisely the identity `ArrayFlatten(S) ≡ COALESCE(scalar-subquery(array_agg over S), [])`, which requires list/ordering semantics for arrays, array_agg's specific algebra (its output being the ordered list of the input's values per the subquery ordering), and a value-returning subquery's NULL-on-empty single-row behavior — none of which exist in QED's bag-semiring model, where all virtual types become integers and aggregates/subqueries are uninterpreted atoms knowable only up to bag equality of their inputs, so the two sides of the rule remain independent symbols whose equality is not a theorem under arbitrary instantiation (and cannot be made one by renaming, since that would collapse the rule to a trivial identity). A DSL extension cannot close this gap: even adding scalar-subquery or coalesce builders, the frozen prover lacks the required list/NULL/cardinality axioms (the same verified limitation behind the AGREE'd sibling scalar.opt subquery rulings), and no narrowing — uncorrelated, single-column, or non-empty input — removes the core "array_agg over S equals the array of S" obligation. ```
