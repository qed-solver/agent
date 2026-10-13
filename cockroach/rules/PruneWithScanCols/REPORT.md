# PruneWithScanCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2
**Scope detail:** fixed three-column WithScan whose outer projection references only columns 0 and 1 through one uninterpreted synthesized column, pruning exactly the never-referenced third column


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneWithScanCols discards columns scanned from the WithScan that are never
used.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneWithScanCols`, not the other rules in that file):

```
# PruneWithScanCols discards columns scanned from the WithScan that are never
# used.
[PruneWithScanCols, Normalize]
(Project
    $input:(WithScan)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $input
            $needed:(UnionCols
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project (PruneCols $input $needed) $projections $passthrough)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures PruneWithScanCols: a shared outer Project built from one uninterpreted projection symbol `Top(w0, w1)` over a shared three-column WithScan scan, where `before()` scans all three columns and `after()` applies the same unmodified Project over the scan with the never-referenced third column dropped (the pruned WithScan is correctly modeled as a two-column projection of the full scan, the only way to express a column-pruned scan in the fixed-schema DSL). `before()` and `after()` are structurally distinct (three-column vs. two-column intermediate), so the proof is a genuine, non-vacuous verification that removing the unreferenced column preserves bag semantics for every scan bag and every instantiation of the uninterpreted symbols, with correct symbol sharing (same `Top`, same source) and no missing preconditions for this instance. The `SCOPE: PARTIAL` tag is honest and specific — fixed arity (3 columns), a single uninterpreted projection expression, and no passthrough columns are genuine restrictions forced by the DSL's fixed-schema scans, not vague or degenerate narrowing, so the result remains a useful, non-trivial special case of the rule.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 0
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 0
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 0
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 0
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 0
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 93750
  }
}
```
