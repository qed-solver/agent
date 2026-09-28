# PruneWindowInputCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 44  **Verification rounds used:** 3
**Scope detail:** one window function, modeled as a per-partition uninterpreted aggregate whose input is the pruned (needed-columns-only) relation in both sides, so the never-used passthrough column is provably absent from the window's aggregate input (encoding the NeededWindowCols precondition by construction).


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneWindowInputCols discards window passthrough columns which are never used.
NB: This rule should go after PruneWindowOutputCols, or else this rule can get
into a cycle.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneWindowInputCols`, not the other rules in that file):

```
# PruneWindowInputCols discards window passthrough columns which are never used.
# NB: This rule should go after PruneWindowOutputCols, or else this rule can get
# into a cycle.
[PruneWindowInputCols, Normalize]
(Project
    $input:(Window $innerInput:* $fn:* $private:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $input
            $needed:(UnionCols3
                (NeededWindowCols $fn $private)
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    (Window (PruneCols $innerInput $needed) $fn $private)
    $projections
    $passthrough
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the logical content of PruneWindowInputCols for per-partition window aggregates: before() has the window's input as (k,v,x) joined back to the per-partition aggregate on k, while after() has it as (k,v) joined back to the same aggregate — the only difference is the unused column x, and the proof is non-vacuous because the SMT solver must verify bag-equivalence of the outer Top(k,v,w) projection across two structurally different join inputs. The restriction to per-partition constant window functions (modeled as an uninterpreted aggregate grouped by the partition key, joined back via INNER equi-join) is a genuine QED/DSL limitation (no Window operator exists in the serializer), is honestly stated in the SCOPE line, and still yields a useful, non-degenerate result covering common windows like SUM/COUNT/MIN/MAX OVER (PARTITION BY k). Symbol sharing is correct (same Top, same perPart, correct joinField ordinals in the concatenated field space), the equi-join on the partition key is appropriately concrete rather than uninterpreted (it is part of the window's bag semantics, not a variable aspect of the rule), and the NeededWindowCols precondition is correctly encoded by construction since the aggregate references only k and v. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 16962749
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 79058373
  },
  "smt_timed_out": false,
  "nontrivial_perms": true,
  "translate_duration": {
    "secs": 0,
    "nanos": 146167
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1184917
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 32195500
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 80615083
  },
  "total_duration": {
    "secs": 0,
    "nanos": 116643000
  }
}
```
