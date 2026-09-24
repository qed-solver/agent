# Name: PruneAggCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneAggCols discards aggregation columns in a GroupBy that are never used.
Note that UpsertDistinctOn is not included here because its columns are always
used.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneAggCols`, not the other rules in that file):

```
# PruneAggCols discards aggregation columns in a GroupBy that are never used.
# Note that UpsertDistinctOn is not included here because its columns are always
# used.
[PruneAggCols, Normalize]
(Project
    $input:(GroupBy | ScalarGroupBy | DistinctOn
            | EnsureDistinctOn
        $innerInput:*
        $aggregations:*
        $groupingPrivate:*
    )
    $projections:*
    $passthrough:* &
        (CanPruneAggCols
            $aggregations
            $needed:(UnionCols
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    ((OpName $input)
        $innerInput
        (PruneAggCols $aggregations $needed)
        $groupingPrivate
    )
    $projections
    $passthrough
)
```
