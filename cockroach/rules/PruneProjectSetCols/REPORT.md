# PruneProjectSetCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 45  **Verification rounds used:** 3
**Scope detail:** the ProjectSet input has one used column and one unused column that is pruned, the zip emits a single column of rows, and the emitted rows depend on the input only through an uninterpreted membership predicate that does not reference the pruned column


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneProjectSetCols discards ProjectSet columns that are never used.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneProjectSetCols`, not the other rules in that file):

```
# PruneProjectSetCols discards ProjectSet columns that are never used.
[PruneProjectSetCols, Normalize]
(Project
    $input:(ProjectSet $innerInput:* $zip:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $input
            $needed:(UnionCols3
                (ZipOuterCols $zip)
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    (ProjectSet (PruneCols $innerInput $needed) $zip)
    $projections
    $passthrough
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully models ProjectSet's row-generating semantics as an INNER join of the input with an uninterpreted emitted-row relation E under an uninterpreted membership predicate M, keeping both M and the outer projection G uninterpreted (not hard-coded) and correctly enforcing the rule's core precondition by ensuring M never references the pruned column L1; before() (L with two columns) and after() (L projected to one column) are structurally distinct and the bag-multiplicity argument holds (each (L0,L1) pair maps to one L0 row, preserving join multiplicities), so the proof is non-vacuous, and the scope is honestly tagged PARTIAL with a specific condition.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7786583
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 67279957
  },
  "smt_timed_out": false,
  "nontrivial_perms": true,
  "translate_duration": {
    "secs": 0,
    "nanos": 894250
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 580333
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 20864417
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 67717000
  },
  "total_duration": {
    "secs": 0,
    "nanos": 104714666
  }
}
```
