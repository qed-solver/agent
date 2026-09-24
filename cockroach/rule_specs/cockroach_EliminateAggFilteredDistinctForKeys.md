# Name: EliminateAggFilteredDistinctForKeys
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateAggFilteredDistinctForKeys is similar to EliminateAggDistinctForKeys,
except that it works when an AggFilter operator is also present.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateAggFilteredDistinctForKeys`, not the other rules in that file):

```
# EliminateAggFilteredDistinctForKeys is similar to EliminateAggDistinctForKeys,
# except that it works when an AggFilter operator is also present.
[EliminateAggFilteredDistinctForKeys, Normalize]
(GroupBy | ScalarGroupBy
    $input:* & (HasStrictKey $input)
    $aggregations:[
        ...
        $item:(AggregationsItem
            (AggFilter (AggDistinct $agg:*) $filter:*)
        )
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
    (ReplaceAggregationsItem
        $aggregations
        $item
        (AggFilter $agg $filter)
    )
    $groupingPrivate
)
```
