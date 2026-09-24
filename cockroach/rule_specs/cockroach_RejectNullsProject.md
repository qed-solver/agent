# Name: RejectNullsProject
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/reject_nulls.opt

RejectNullsProject adds a "col IS NOT NULL" null-rejecting filter to the input
of a project if the following conditions hold:
1. The parent Select operator rejects nulls on a synthesized (projection)
column.
2. At least one of the input columns of the projection is in the
RejectNullCols ColSet of the input of the Project.
3. The projection 'transmits' NULLS from the candidate column; if the column
is NULL, the output of the projection is NULL.

Extracted from `reject_nulls.opt` (which defines multiple rules — implement specifically `RejectNullsProject`, not the other rules in that file):

```
# RejectNullsProject adds a "col IS NOT NULL" null-rejecting filter to the input
# of a project if the following conditions hold:
#   1. The parent Select operator rejects nulls on a synthesized (projection)
#      column.
#   2. At least one of the input columns of the projection is in the
#      RejectNullCols ColSet of the input of the Project.
#   3. The projection 'transmits' NULLS from the candidate column; if the column
#      is NULL, the output of the projection is NULL.
[RejectNullsProject, Normalize]
(Select
    $input:(Project
            $innerInput:*
            $projections:* &
                ^(ColsAreEmpty
                    $projectionCols:(ProjectionCols $projections)
                )
            $passthrough:*
        ) &
        ^(ColsAreEmpty $rejectNullCols:(RejectNullCols $input))
    $filters:* &
        ^(ColsAreEmpty
            $nullRejectedCols:(IntersectionCols
                (IntersectionCols
                    $projectionCols
                    $rejectNullCols
                )
                (GetNullRejectedCols $filters)
            )
        )
)
=>
(Select
    (Project
        (Select
            $innerInput
            [
                (FiltersItem
                    (IsNot
                        (NullRejectProjections
                            $projections
                            $nullRejectedCols
                            (RejectNullCols $innerInput)
                        )
                        (Null (AnyType))
                    )
                )
            ]
        )
        $projections
        $passthrough
    )
    $filters
)
```
