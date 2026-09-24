# Name: PushAggDistinctIntoGroupBy
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/groupby.opt

PushAggDistinctIntoGroupBy pushes an aggregate function DISTINCT modifier into
the input of a GroupBy or ScalarGroupBy operator. This allows the optimizer to
take advantage of an index on the column(s) subject to the DISTINCT operation.
PushAggDistinctIntoGroupBy can match any single aggregate function, including
those that have multiple input arguments.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `PushAggDistinctIntoGroupBy`, not the other rules in that file):

```
# PushAggDistinctIntoGroupBy pushes an aggregate function DISTINCT modifier into
# the input of a GroupBy or ScalarGroupBy operator. This allows the optimizer to
# take advantage of an index on the column(s) subject to the DISTINCT operation.
# PushAggDistinctIntoGroupBy can match any single aggregate function, including
# those that have multiple input arguments.
[PushAggDistinctIntoGroupBy, Normalize]
(GroupBy | ScalarGroupBy
    $input:*
    $aggregations:[
        $item:(AggregationsItem (AggDistinct $agg:*) $aggColID:*)
    ]
    $groupingPrivate:*
)
=>
((OpName)
    (DistinctOn
        $input
        (MakeAggCols
            FirstAgg
            (OrderingCols
                (ExtractGroupingOrdering $groupingPrivate)
            )
        )
        (MakeGrouping
            (UnionCols
                (GroupingCols $groupingPrivate)
                (ExtractAggInputColumns $agg)
            )
            (EmptyOrdering)
        )
    )
    [ (AggregationsItem $agg $aggColID) ]
    $groupingPrivate
)
```
