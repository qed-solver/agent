# Name: PushLimitIntoOrdinality
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/limit.opt

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
