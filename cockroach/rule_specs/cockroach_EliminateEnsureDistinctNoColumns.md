# Name: EliminateEnsureDistinctNoColumns
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateEnsureDistinctNoColumns is similar to EliminateDistinctNoColumns,
except that Max1Row will raise an error if there are no grouping columns and
the input has more than one row. No grouping columns means there is at most
one group. And the Max1Row operator is needed to raise an error if that group
has more than one row, which is a requirement of the EnsureDistinct and
EnsureUpsertDistinct operators.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateEnsureDistinctNoColumns`, not the other rules in that file):

```
# EliminateEnsureDistinctNoColumns is similar to EliminateDistinctNoColumns,
# except that Max1Row will raise an error if there are no grouping columns and
# the input has more than one row. No grouping columns means there is at most
# one group. And the Max1Row operator is needed to raise an error if that group
# has more than one row, which is a requirement of the EnsureDistinct and
# EnsureUpsertDistinct operators.
[EliminateEnsureDistinctNoColumns, Normalize]
(EnsureDistinctOn | EnsureUpsertDistinctOn
    $input:*
    $aggregations:*
    $groupingPrivate:* & (HasNoGroupingCols $groupingPrivate)
)
=>
(ConstructProjectionFromDistinctOn
    (Max1Row $input (ErrorOnDup $groupingPrivate))
    (MakeEmptyColSet)
    $aggregations
)
```
