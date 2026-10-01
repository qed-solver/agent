# PushFilterIntoJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 26  **Verification rounds used:** 2
**Scope detail:** INNER join only, a single parent-predicate conjunct that references


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_filter.rs, lines 541-576
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-vacuous capture of the exact sub-case it claims: a single filter conjunct over the left input's column (one uninterpreted `left_filter` shared by name above the join and on the left child, with both occurrences resolving to L's column) is moved from above an INNER join — whose condition is a shared uninterpreted `join_cond` — down to the left child, which is precisely the transformation DataFusion's `push_down_join` performs for its left-only classification, and the universally-proven claim is sound with no missing preconditions (no null-extension or key assumptions are involved for INNER join). `before()` and `after()` are structurally different plans (the filter's position is the whole point), so the proof is not vacuous, and the only hard-codings that narrow the rule (INNER-only join type, single conjunct, left-only columns — versus the source's multi-type, multi-category behavior) are fully disclosed in a specific, concrete `// SCOPE: PARTIAL` line rather than hidden, leaving the result an honest, useful, non-degenerate special case rather than a misleadingly "general" one.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6765166
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 33724875
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 822250
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 501250
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18606750
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 33824917
  },
  "total_duration": {
    "secs": 0,
    "nanos": 67874791
  }
}
```
