# Name: PushAggFilterIntoScalarGroupBy
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/groupby.opt

PushAggFilterIntoScalarGroupBy pushes an aggregate function FILTER
modifier into the input of the ScalarGroupBy operator. This allows the
optimizer to take advantage of an index on the column(s) subject to the
FILTER operation. PushAggFilterIntoScalarGroupBy can match any single
aggregate function, including those that have multiple input arguments.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `PushAggFilterIntoScalarGroupBy`, not the other rules in that file):

```
# PushAggFilterIntoScalarGroupBy pushes an aggregate function FILTER
# modifier into the input of the ScalarGroupBy operator. This allows the
# optimizer to take advantage of an index on the column(s) subject to the
# FILTER operation. PushAggFilterIntoScalarGroupBy can match any single
# aggregate function, including those that have multiple input arguments.
[PushAggFilterIntoScalarGroupBy, Normalize]
(ScalarGroupBy
    $input:*
    $aggregations:[
        $item:(AggregationsItem
            (AggFilter $agg:* $condition:*)
            $aggColID:*
        )
    ]
    $groupingPrivate:*
)
=>
(ScalarGroupBy
    (Select $input [ (FiltersItem $condition) ])
    [ (AggregationsItem $agg $aggColID) ]
    $groupingPrivate
)
```
