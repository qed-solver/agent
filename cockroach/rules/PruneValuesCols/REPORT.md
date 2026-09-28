# PruneValuesCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** fixed single-row three-column Values pruned to the two referenced columns, modeled with concrete literal content.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneValuesCols discards Values columns that are never used.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneValuesCols`, not the other rules in that file):

```
# PruneValuesCols discards Values columns that are never used.
[PruneValuesCols, Normalize]
(Project
    $input:(Values)
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

The encoding reproduces the source rule's genuine shape — an outer Project (uninterpreted `Top` on one column, passthrough on the other) over a Values whose unreferenced third column is removed — and it is not vacuous: the after-side constant (1,2) must be exactly the projection of (1,2,3), a link SMT genuinely checks (a wrong pruned tuple would refute it), and `before()`/`after()` are structurally different plans. The narrowing to a concrete single-row 3→2-column literal Values is forced by QED's semantics rather than a missed uninterpreted symbol: Values contents are always concrete constants in the QED model (there is no symbolic-values symbol to quantify over, and substituting an independent uninterpreted scan on the after side would leave the constant-to-constant link inexpressible and would instead prove a different, project-pushing rewrite with the wrong operator shape). The `// SCOPE: PARTIAL` line accurately and specifically names the restriction (fixed single row, fixed 3-column→2-column pruning, literal content), the `CanPruneCols` guard is a rule-firing condition rather than a soundness assumption, and the result remains a non-degenerate proof of the rule's core transformation, so the PROVABLE verdict is meaningful and not misleading. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 4300875
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 25627834
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 845583
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 307625
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 13907292
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 25654500
  },
  "total_duration": {
    "secs": 0,
    "nanos": 54671833
  }
}
```
