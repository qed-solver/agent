# PruneScanCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** fixed three-column scan whose outer projection references only columns 0 and 1 through one uninterpreted synthesized column, pruning exactly the never-referenced third column


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneScanCols discards Scan operator columns that are never used. The needed
columns are pushed down into the Scan's opt.ScanOpDef private.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneScanCols`, not the other rules in that file):

```
# PruneScanCols discards Scan operator columns that are never used. The needed
# columns are pushed down into the Scan's opt.ScanOpDef private.
[PruneScanCols, Normalize]
(Project
    $input:(Scan)
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

The encoding faithfully captures the semantic core of PruneScanCols — inserting a column-pruning projection below an outer projection that doesn't reference all scan columns — with a non-vacuous before/after difference (2-operator vs. 3-operator plans). The uninterpreted Top operator correctly stands in for an arbitrary projection expression over the needed columns, and the shared source scan plus correct field-index remapping through the pruned relation are handled properly. The PARTIAL scope is honestly declared and reflects a genuine DSL limitation (no way to parametrize over column count or the dynamically-computed $needed set), while the chosen 3-column/1-unused instance is the minimal non-degenerate case that exercises the rule's actual transformation. ```

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
    "nanos": 107291
  }
}
```
