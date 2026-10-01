# OptimizeProjections

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 21  **Verification rounds used:** 2
**Scope detail:** only the local fragment pruning one unused uninterpreted expression from a two-layer projection above a fixed 3-column scan (the full rule also recurses through the plan, adds pre-join projections, and removes identity projections).


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/optimize_projections/mod.rs, lines 1-899
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` (two stacked projections, the outer dropping column 2) and `after()` (one projection) are structurally distinct, and their difference is exactly the elimination of the dead `FC` computation — the rule's core "eliminate unused columns / merge consecutive projections" behavior — so the proof is non-vacuous, with symbols correctly shared (same `exprA`/`exprB`/`exprC` instances in both sides, distinct `FA`/`FB`/`FC` operators and scan) and no hidden preconditions (projection merge is unconditionally valid in bag semantics; all types nullable, scan non-unique). The fixed 3-column/two-layer shape is an inherent limit of expressing a recursive plan rule as a finite DSL pattern, is honestly and specifically tagged `PARTIAL`, and captures a genuine, non-degenerate fragment of the real rewrite (the same shape DataFusion's `merge_consecutive_projections` folds).

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
    "nanos": 334125
  }
}
```
