# Name: PushSelectIntoGroupBy
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/select.opt

PushSelectIntoGroupBy pushes a Select condition below a GroupBy in the case
where it only references grouping columns or ConstAgg columns.

This rule doesn't work on ScalarGroupBy which exhibits different behavior if
the input is empty:
SELECT MAX(y) FROM a

If "a" is empty, this returns a single row containing a null value. This is
different behavior than a GroupBy with grouping columns, which would return
the empty set for a similar query:
SELECT MAX(y) FROM a GROUP BY x

Citations: [2]

Note: Do not add EnsureDistinctOn to the match pattern. Pushing the select
filters through the EnsureDistinctOn can prevent it from detecting duplicate
rows and therefore change error behavior.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushSelectIntoGroupBy`, not the other rules in that file):

```
# PushSelectIntoGroupBy pushes a Select condition below a GroupBy in the case
# where it only references grouping columns or ConstAgg columns.
#
# This rule doesn't work on ScalarGroupBy which exhibits different behavior if
# the input is empty:
#   SELECT MAX(y) FROM a
#
# If "a" is empty, this returns a single row containing a null value. This is
# different behavior than a GroupBy with grouping columns, which would return
# the empty set for a similar query:
#   SELECT MAX(y) FROM a GROUP BY x
#
# Citations: [2]
#
# Note: Do not add EnsureDistinctOn to the match pattern. Pushing the select
# filters through the EnsureDistinctOn can prevent it from detecting duplicate
# rows and therefore change error behavior.
[PushSelectIntoGroupBy, Normalize]
(Select
    $input:(GroupBy | DistinctOn
        $groupingInput:*
        $aggregations:*
        $groupingPrivate:*
    )
    $filters:[
        ...
        $item:* &
            (IsBoundBy
                $item
                $passthrough:(GroupingAndConstCols
                    $groupingPrivate
                    $aggregations
                )
            )
        ...
    ]
)
=>
(Select
    ((OpName $input)
        (Select
            $groupingInput
            (ExtractBoundConditions $filters $passthrough)
        )
        $aggregations
        $groupingPrivate
    )
    (ExtractUnboundConditions $filters $passthrough)
)
```
