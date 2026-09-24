# Name: RejectNullsGroupBy
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/reject_nulls.opt

RejectNullsGroupBy pushes a "col IS NOT NULL" null-rejecting filter below the
GroupBy operator if it allows null rejection for that column (i.e. if it's in
the NullRejectCols set). See ruleProps.buildGroupByProps for more details on
the criteria for setting NullRejectCols. See the file header comment for more
information on null rejection.

This rule is important for decorrelation in cases similar to this:

SELECT * FROM a WHERE (SELECT MIN(z) FROM b WHERE a.x=b.x) = a.y

The top-level "= a.y" filter rejects NULL values in the b.z column, which ends
up in the right side of a LeftJoin operator, thus enabling it to be mapped to
an InnerJoin operator.

This rule is not useful for DistinctOn: it can only fire if there are no
FirstAgg aggregates, but in that case the filter would have gotten pushed
through DistinctOn.

This rule is marked as low priority so that it runs after Select filter
pushdown rules. If a filter can be pushed down in its entirety, that's
preferable to synthesizing a new "col IS NOT NULL" filter.

Extracted from `reject_nulls.opt` (which defines multiple rules — implement specifically `RejectNullsGroupBy`, not the other rules in that file):

```
# RejectNullsGroupBy pushes a "col IS NOT NULL" null-rejecting filter below the
# GroupBy operator if it allows null rejection for that column (i.e. if it's in
# the NullRejectCols set). See ruleProps.buildGroupByProps for more details on
# the criteria for setting NullRejectCols. See the file header comment for more
# information on null rejection.
#
# This rule is important for decorrelation in cases similar to this:
#
#   SELECT * FROM a WHERE (SELECT MIN(z) FROM b WHERE a.x=b.x) = a.y
#
# The top-level "= a.y" filter rejects NULL values in the b.z column, which ends
# up in the right side of a LeftJoin operator, thus enabling it to be mapped to
# an InnerJoin operator.
#
# This rule is not useful for DistinctOn: it can only fire if there are no
# FirstAgg aggregates, but in that case the filter would have gotten pushed
# through DistinctOn.
#
# This rule is marked as low priority so that it runs after Select filter
# pushdown rules. If a filter can be pushed down in its entirety, that's
# preferable to synthesizing a new "col IS NOT NULL" filter.
[RejectNullsGroupBy, Normalize, LowPriority]
(Select
    $input:(GroupBy | ScalarGroupBy
        $innerInput:*
        $aggregations:*
        $groupingPrivate:*
    )
    $filters:* &
        (HasNullRejectingFilter
            $filters
            $rejectCols:(RejectNullCols $input)
        )
)
=>
(Select
    ((OpName $input)
        (Select
            $innerInput
            [
                (FiltersItem
                    (IsNot
                        (NullRejectAggVar
                            $aggregations
                            $rejectCols
                        )
                        (Null (AnyType))
                    )
                )
            ]
        )
        $aggregations
        $groupingPrivate
    )
    $filters
)
```
