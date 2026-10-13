# PruneWindowOutputCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 28  **Verification rounds used:** 3
**Scope detail:** window functions modeled as partition-determined per-partition uninterpreted aggregates joined back to the input (no frame/ordering semantics, same modeling as FoldGroupByAndWindow); fixed shape with exactly two window functions, the one needed by the outer projection is kept and the other pruned.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneWindowOutputCols eliminates unused window functions from a Window
expression.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneWindowOutputCols`, not the other rules in that file):

```
# PruneWindowOutputCols eliminates unused window functions from a Window
# expression.
[PruneWindowOutputCols, Normalize]
(Project
    (Window $input:* $windows:* $private:*)
    $projections:*
    $passthrough:* &
        (CanPruneWindows
            $needed:(UnionCols
                (ProjectionOuterCols $projections)
                $passthrough
            )
            $windows
        )
)
=>
(Project
    (Window $input (PruneWindows $needed $windows) $private)
    $projections
    $passthrough
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` truly differs from `after()` only by computing an extra unused window function `w2` (two aggregate calls vs one in the per-partition window, then dropped by the identical outer projection), which is exactly the pruning content of `PruneWindowOutputCols`, and the `input ⋈ (grouped-by-k aggregate)` model preserves one-output-row-per-input-row cardinality, so the proof is non-vacuous and the reused `cond`/field indices resolve correctly in both plans; the fixed two-column input and exactly-two-functions (one kept, one pruned) shape is a genuine, honestly-declared consequence of QED having no bag semantics for `Window` (no frame/ordering), so the PARTIAL scope tag is accurate and the result is a faithful, non-degenerate special case rather than a vacuous or over-constrained one.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 15762791
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36416958
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 902125
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1170250
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 34544375
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36548917
  },
  "total_duration": {
    "secs": 0,
    "nanos": 87225791
  }
}
```
