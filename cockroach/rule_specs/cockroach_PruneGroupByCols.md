# Name: PruneGroupByCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneGroupByCols discards GroupBy input columns that are never used. Note that
UpsertDistinctOn is not included here because its columns are always used.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneGroupByCols`, not the other rules in that file):

```
# PruneGroupByCols discards GroupBy input columns that are never used. Note that
# UpsertDistinctOn is not included here because its columns are always used.
[PruneGroupByCols, Normalize]
(GroupBy | ScalarGroupBy | DistinctOn | EnsureDistinctOn
    $input:*
    $aggregations:*
    $groupingPrivate:* &
        (CanPruneCols
            $input
            $needed:(UnionCols
                (AggregationOuterCols $aggregations)
                (NeededGroupingCols $groupingPrivate)
            )
        )
)
=>
((OpName)
    (PruneCols $input $needed)
    $aggregations
    (PruneOrderingGroupBy $groupingPrivate $needed)
)
```
