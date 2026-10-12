# PartitionPruning

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 89  **Verification rounds used:** 6
**Scope detail:** assumes the inner join's (uninterpreted) condition is exactly the partition-column equi-join, so the DPP pruning filter is the semi-join under that same condition


## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/dynamicpruning/PartitionPruning.scala, lines 52-373
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully abstracts DPP's semantic effect — the inserted pruning filter keeps exactly the build rows whose join-key exists on the other side (a SEMI join), which is then re-joined under the same condition — and QED's proof of `build ⋈ probe = (build ▷ probe) ⋈ probe` is a genuine, non-vacuous identity (the two plans are structurally different) that is precisely the losslessness invariant that makes DPP sound. The assumptions (INNER join, build-side pruning, join condition identical to the pruning condition, deterministic scans) are inherent to how DPP works rather than artificial over-constraints, the PARTIAL scope tag is honest and specific, and the source's cost/benefit guards do not bear on the semantic equivalence being proved.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8256501
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 37754000
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1043208
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 538708
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21303542
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 37852334
  },
  "total_duration": {
    "secs": 0,
    "nanos": 74873583
  }
}
```
