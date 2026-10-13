# PruneSemiAntiJoinRightCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 4
**Scope detail:** assumes an uncorrelated anti-join with a 1-column left, a 2-column right of which one column is pruned, and an uninterpreted on-clause over the kept (left, right-key) pair


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneSemiAntiJoinRightCols discards columns on the right side of a
Semi or Anti join that are never used. This is similar to PruneJoinRightCols.
PruneJoinRightCols normally prunes the RHS of a join but it can't do that
in the case of Semi/Anti joins because the projection is eliminated after
the LHS is pruned. This rule doesn't require a projection over the Semi/Anti
join in order to prune the RHS.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneSemiAntiJoinRightCols`, not the other rules in that file):

```
# PruneSemiAntiJoinRightCols discards columns on the right side of a
# Semi or Anti join that are never used. This is similar to PruneJoinRightCols.
# PruneJoinRightCols normally prunes the RHS of a join but it can't do that
# in the case of Semi/Anti joins because the projection is eliminated after
# the LHS is pruned. This rule doesn't require a projection over the Semi/Anti
# join in order to prune the RHS.
[PruneSemiAntiJoinRightCols, Normalize]
(SemiJoin | SemiJoinApply | AntiJoin | AntiJoinApply
    $left:*
    $right:*
    $on:*
    $private:* &
        (CanPruneCols $right $needed:(FilterOuterCols $on))
)
=>
((OpName) $left (PruneCols $right $needed) $on $private)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a genuine, non-trivial partial instance of PruneSemiAntiJoinRightCols: before() is an ANTI join over a 2-column right side where only col0 appears in the uninterpreted predicate, and after() projects the right to just col0 — the structural difference is real (right arity changes from 2 to 1), the predicate symbol is correctly shared, and ANTI is one of the four join kinds the source rule covers. The SCOPE line honestly and specifically documents the restrictions (only ANTI, fixed 1-col left / 2-col right, uncorrelated), and the proof confirms the core semantic principle (right-side columns not referenced by the join condition can be eliminated in a semi/anti join) is universally valid.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9035626
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34436208
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 887792
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 540750
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 22342333
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34547417
  },
  "total_duration": {
    "secs": 0,
    "nanos": 72349667
  }
}
```
