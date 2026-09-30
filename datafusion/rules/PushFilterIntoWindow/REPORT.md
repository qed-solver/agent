# PushFilterIntoWindow

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** the window is modeled as a partition-determined uninterpreted aggregate joined back to its input on a single partition column (no frame/ordering semantics, single window function, two-column input), the filter is a conjunction of exactly one conjunct referencing only the partition column (the conjuncts DataFusion pushes below the window, its potential-partition-keys precondition encoded by construction) and one conjunct over the window output row (kept above).


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_filter.rs, lines 1068-1139
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures DataFusion's core semantics — a conjunct P referencing only the partition column k (constant per partition, so pushable) is moved below a window modeled as an uninterpreted partition-aggregate join-back (the right stand-in since QED can't model frames/ordering), while a kept conjunct Q over the full window row (k, v, w) stays above; P, Q, and the window value w are correctly shared uninterpreted symbols on both sides (not baked-in constants), and before()/after() are structurally distinct plans, so the ~90ms proof is a genuine, non-vacuous universal argument rather than an artifact of over-constraint. The `SCOPE: PARTIAL` line is honest and specific — single partition column, single window function, no frame/ordering, two-column input, one pushed + one kept conjunct — and each restriction is a real QED/DSL limitation (window functions have no bag semantics; the per-conjunct split and multi-key/multi-window details are control-flow the uninterpreted P/Q already abstract) rather than an encoding error, leaving a faithful, non-degenerate special case.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 12201875
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 48197083
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 869833
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1039542
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 28083875
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 48570542
  },
  "total_duration": {
    "secs": 0,
    "nanos": 92846625
  }
}
```
