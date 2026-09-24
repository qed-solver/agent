# Name: EliminateAggDistinctForKeys
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateAggDistinctForKeys eliminates unnecessary AggDistinct modifiers when
it is known that the aggregation argument is unique within each group.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateAggDistinctForKeys`, not the other rules in that file):

```
# EliminateAggDistinctForKeys eliminates unnecessary AggDistinct modifiers when
# it is known that the aggregation argument is unique within each group.
[EliminateAggDistinctForKeys, Normalize]
(GroupBy | ScalarGroupBy
    $input:* & (HasStrictKey $input)
    $aggregations:[
        ...
        $item:(AggregationsItem (AggDistinct $agg:*))
        ...
    ]
    $groupingPrivate:* &
        (CanRemoveAggDistinctForKeys
            $input
            $groupingPrivate
            $agg
        )
)
=>
((OpName)
    $input
    (ReplaceAggregationsItem $aggregations $item $agg)
    $groupingPrivate
)
```
