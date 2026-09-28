# PruneProjectCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** fixed three-column inner project whose outer references only the passthrough and one synthesized column, pruning exactly the unused third column.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneProjectCols discards columns from a nested project which are not used by
the outer project.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneProjectCols`, not the other rules in that file):

```
# PruneProjectCols discards columns from a nested project which are not used by
# the outer project.
[PruneProjectCols, Normalize]
(Project
    $project:(Project)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $project
            $needed:(UnionCols
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project (PruneCols $project $needed) $projections $passthrough)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-vacuous special case of PruneProjectCols — `before()` is a Scan → inner-Project(3 cols: `col0`, `Used(col0)`, `Unused(col0)`) → outer-Project(`Top(inner.col0, inner.col1)`), while `after()` is the equivalent Scan → Project(`Top(col0, Used(col0))`), so the proof genuinely captures "drop the nested project's never-referenced column" (plus the consequent merge of the now-pure inner project) rather than a structurally identical triviality. Symbols are shared correctly (the same `top` operator and the same `passthrough`/`usedSyn` expressions appear on both sides, while `unusedSyn` appears only in `before()`), and the omitted `CanPruneCols` is an optimizer bookkeeping guard, not a semantic precondition, so its absence does not change the claim; the `PARTIAL` tag honestly and specifically names the three-column/one-unused-column restriction, and QED proves the equivalence for all instantiations of the uninterpreted table and functions.

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
    "nanos": 460708
  }
}
```
