# CostBasedJoinReorder

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** a single concrete 4-way inner-join reordering instance (left-deep ((A⋈B)⋈C)⋈D to bushy (A⋈B)⋈(C⋈D)) with fixed pairwise uninterpreted conditions and single-column inputs, rather than the DP cost model, condition placement, and output-order handling


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/CostBasedJoinReorder.scala, lines 36-102
```

## Independent verifier review

**Verdict:** CONFIRMED

It is a faithful, non-vacuous instance — `before()` is left-deep ((A⋈B)⋈C)⋈D and `after()` is bushy (A⋈B)⋈(C⋈D); the four independent uninterpreted conjuncts bind to the same (table,tbl) columns in consistent argument order on both sides, and `pCD` genuinely moves from the root into the C⋈D subtree, so the proof is of real tree reordering plus condition re-placement, not a structural identity. INNER is the correct kind (the sound members of `InnerLike`), the `joinField` ordinals are correct, and inner-join reordering requires no key/NOT-NULL precondition. The fixed 4-way left-deep→bushy shape and single-column inputs are an honestly-labeled, non-degenerate PARTIAL scope rather than a coincidental or vacuous proof.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 10747957
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35240708
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1157750
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 800917
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 26267375
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35395000
  },
  "total_duration": {
    "secs": 0,
    "nanos": 78223209
  }
}
```
