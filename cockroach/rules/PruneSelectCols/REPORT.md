# PruneSelectCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** fixed three-column scan input whose Select filter and outer projection reference only columns 0 and 1, pruning exactly the never-referenced third column.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneSelectCols discards Select input columns that are never used.

The PruneCols property should prevent this rule (which pushes Project below
Select) from cycling with the PushSelectIntoProject rule (which pushes Select
below Project).

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneSelectCols`, not the other rules in that file):

```
# PruneSelectCols discards Select input columns that are never used.
#
# The PruneCols property should prevent this rule (which pushes Project below
# Select) from cycling with the PushSelectIntoProject rule (which pushes Select
# below Project).
[PruneSelectCols, Normalize]
(Project
    (Select $input:* $filters:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $input
            $needed:(UnionCols3
                (FilterOuterCols $filters)
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    (Select (PruneCols $input $needed) $filters)
    $projections
    $passthrough
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous and faithful: `before()` = π_Top(σ_F(S₃)) and `after()` = π_Top(σ_F(π_{0,1}(S₃))) are structurally distinct trees, and the provable claim is a genuine universal bag-semantic identity (the rewrite preserves multiplicity row-for-row since F and Top ignore the pruned column). The uninterpreted symbols F and Top are correctly shared between both sides with exact column correspondence (columns 0,1 of the 3-column input under the pruning projection), and by constructing them over only columns 0/1 the encoding captures the source rule's `CanPruneCols` precondition — the pruned column is unreferenced — by construction rather than silently dropping it. The plan shape matches `PruneSelectCols` (outer Project and Select unchanged, new Project inserted between Select and its input, with a Scan input satisfying the "projection can merge" condition), and the disclosed PARTIAL scope — a fixed three-column scan whose filter and projection reference exactly columns 0,1 — is a specific, non-degenerate special case that is essentially forced, since QED cannot quantify over which columns an uninterpreted symbol happens to reference.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5881458
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 32928125
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 814291
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 462375
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16867167
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 33045417
  },
  "total_duration": {
    "secs": 0,
    "nanos": 65426291
  }
}
```
