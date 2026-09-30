# PushLimitIntoOrdinality

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 32  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/limit.opt

PushLimitIntoOrdinality pushes the Limit operator into the Ordinality
operator when the ordering associated with both operators allows it.

Pushing the limit as far as possible down the tree shouldn't have negative
effects, but will reduce the number of rows processed by operators higher up,
and if the limit is pushed all the way down to a scan, the scan can be limited
directly.

In order to prevent this rule from affecting:
1. the set of rows kept by the limit,
2. the ordinals assigned to those rows by the ordinality, and
3. the final ordering of the rows,
the new limit's ordering should be "extended" to imply the ordinality's
ordering, so it is set to be an intersection of the original limit ordering
and the ordinality's ordering (see OrderingChoice.Intersection).

Extracted from `limit.opt` (which defines multiple rules — implement specifically `PushLimitIntoOrdinality`, not the other rules in that file):

```
# PushLimitIntoOrdinality pushes the Limit operator into the Ordinality
# operator when the ordering associated with both operators allows it.
#
# Pushing the limit as far as possible down the tree shouldn't have negative
# effects, but will reduce the number of rows processed by operators higher up,
# and if the limit is pushed all the way down to a scan, the scan can be limited
# directly.
#
# In order to prevent this rule from affecting:
#   1. the set of rows kept by the limit,
#   2. the ordinals assigned to those rows by the ordinality, and
#   3. the final ordering of the rows,
# the new limit's ordering should be "extended" to imply the ordinality's
# ordering, so it is set to be an intersection of the original limit ordering
# and the ordinality's ordering (see OrderingChoice.Intersection).
[PushLimitIntoOrdinality, Normalize]
(Limit
    (Ordinality $input:* $private:*)
    $limit:*
    $limitOrdering:* &
        (OrderingCanProjectCols
            $limitOrdering
            (OutputCols $input)
        ) &
        (OrderingIntersects
            (OrdinalityOrdering $private)
            $limitOrdering
        )
)
=>
(Ordinality
    (Limit
        $input
        $limit
        (OrderingIntersection
            (OrdinalityOrdering $private)
            $limitOrdering
        )
    )
    $private
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests entirely on list/ordering semantics: the Limit keeps rows positionally (first N in an ordering), the Ordinality's output column is position-dependent, and the OrderingIntersects/OrderingIntersection premises are what guarantee the same rows survive with the same ordinal values after the push-down. QED reasons only over bag semantics and treats Sort/Limit/Offset as uninterpreted operators with no positional algebra (qed.pdf §6.2 "list semantics" failure category), so any bag-level encoding is either vacuous (both sides collapse to the same expression) or unfaithful (e.g., modeling Limit as a row predicate and Ordinality as an uninterpreted row function would prove a different, trivially true filter/projection commutation, not this rule). The absence of a Limit/Sort builder in RelRN is not the decisive gap — even adding one (JSONSerializer already serializes LogicalSort) would leave QED with an uninterpreted operator, and the before/after equivalence is simply not a bag-semantic identity. ```
