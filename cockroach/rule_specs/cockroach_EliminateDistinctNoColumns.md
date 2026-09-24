# Name: EliminateDistinctNoColumns
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateDistinctNoColumns eliminates a distinct operator with no grouping
columns, replacing it with a projection and a LIMIT 1. For example:
SELECT DISTINCT ON (a) a, b FROM ab WHERE a=1
is equivalent to:
SELECT a, b FROM ab WHERE a=1 LIMIT 1

Note that this rule does not apply to EnsureDistinctOn or
EnsureUpsertDistinctOn, since they will raise an error if there are duplicate
rows.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateDistinctNoColumns`, not the other rules in that file):

```
# EliminateDistinctNoColumns eliminates a distinct operator with no grouping
# columns, replacing it with a projection and a LIMIT 1. For example:
#   SELECT DISTINCT ON (a) a, b FROM ab WHERE a=1
# is equivalent to:
#   SELECT a, b FROM ab WHERE a=1 LIMIT 1
#
# Note that this rule does not apply to EnsureDistinctOn or
# EnsureUpsertDistinctOn, since they will raise an error if there are duplicate
# rows.
[EliminateDistinctNoColumns, Normalize]
(DistinctOn | UpsertDistinctOn
    $input:*
    $aggregations:*
    $groupingPrivate:* & (HasNoGroupingCols $groupingPrivate)
)
=>
(ConstructProjectionFromDistinctOn
    (Limit
        $input
        (IntConst (DInt 1))
        (GroupingInputOrdering $groupingPrivate)
    )
    (MakeEmptyColSet)
    $aggregations
)
```
