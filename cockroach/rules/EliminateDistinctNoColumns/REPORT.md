# EliminateDistinctNoColumns

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

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
```

## Independent verifier review

**Verdict:** AGREE

The rule’s validity depends on first-row semantics: a `DistinctOn` with no grouping columns returns the first input row under a specific ordering, and the RHS uses `LIMIT 1` on that ordering. QED has no bag-semantic model for `Sort`/`Limit`/ordering and treats aggregates such as first-value aggregation as uninterpreted, so it cannot relate either side to a particular member of the input bag.
