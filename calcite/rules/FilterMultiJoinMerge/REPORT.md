# FilterMultiJoinMerge

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 8  **Verification rounds used:** 1
**Scope detail:** the MultiJoin is an inner-only (INNER pairwise joins, no


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterMultiJoinMergeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The before() and after() trees are structurally different (a Filter node above the root join vs. the same two uninterpreted predicates ANDed into the root join condition), P and F are correctly shared symbols over the full (A,B,C) row, and A/B/C are three independent scans — so the proof is a genuine, non-vacuous verification of the rule's core rewrite (filter composed into the MultiJoin's post-join filter) for the case where that filter coincides with the root join condition. This is a faithfully and honestly tagged PARTIAL encoding: inner-only join types, fixed three-factor shape, and a non-null post-join filter are the stated restrictions, and the inner-only limit is forced by QED's binary-join modeling (an outer join's post-join filter cannot be folded into its condition), not porter laziness. The only blemish is a mildly misleading SCOPE clause — the real rule also merely ANDs rather than distributing conjuncts — which understates rather than overclaims generality, so the "provable" result remains meaningful.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9159041
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34628417
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 869542
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 625125
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 24905875
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34826041
  },
  "total_duration": {
    "secs": 0,
    "nanos": 75955542
  }
}
```
