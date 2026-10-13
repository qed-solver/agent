# PruneAggCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** fixed 3-column scan (group key plus two aggregate arguments), one group key, and an outer project that selects only the group key and the retained single-arg aggregate f so the pruned single-arg aggregate g is never used.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

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
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-degenerate special case of PruneAggCols: before() has Aggregate(key; f(a), g(b)) projected to (key, f), and after() has Aggregate(key; f(a)) projected to (key, f) — a genuine structural difference that captures the rule's core transformation (removing an unused aggregation from a GroupBy when the outer project doesn't reference it). The shared uninterpreted aggregate symbol `f` is correctly reused across both sides (matching the source rule's requirement that kept aggregations are unchanged), `g` appears only in before(), and the SCOPE: PARTIAL tag honestly and specifically names the restrictions (fixed 3-column scan, one group key, one retained and one pruned single-arg aggregate). QED's fast proof is consistent with this being a structural bag-semantic equivalence (dropping an unreferenced column from a group-by's output doesn't change the remaining columns' multiset), not a vacuous or coincidental result. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 13212959
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35114958
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 944417
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 822541
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 30740708
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35221625
  },
  "total_duration": {
    "secs": 0,
    "nanos": 81950583
  }
}
```
