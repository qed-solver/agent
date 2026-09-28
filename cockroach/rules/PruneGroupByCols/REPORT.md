# PruneGroupByCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** fixed 4-column scan (one group key, two aggregate arguments, one unused column), one group key, two non-distinct single-argument aggregates, and the outer projection selects all aggregate output columns.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

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
```

## Independent verifier review

**Verdict:** CONFIRMED

It is a nontrivial partial instance of PruneGroupByCols: the unused fourth input column is removed before the same uninterpreted GROUP BY/aggregates, while the group key and aggregate arguments are preserved. The added all-column projection is an identity context, and the PARTIAL line honestly states the fixed arity/aggregate assumptions.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 13121291
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 54175207
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1338500
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1029667
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 30548333
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 54447042
  },
  "total_duration": {
    "secs": 0,
    "nanos": 100798333
  }
}
```
