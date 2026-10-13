# RowLevelOperationRuntimeGroupFiltering

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 86  **Verification rounds used:** 6
**Scope detail:** only WriteDelta row-level operations whose delta query is a single direct Filter(cond, scan) over a non-nullable key column, with the runtime group filter derived from the same scan+cond (idealized as an exact semi-join on the key; grouping/projection omitted as row-preserving).


## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/dynamicpruning/RowLevelOperationRuntimeGroupFiltering.scala, lines 49-169
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's core invariant — injecting a runtime group filter (semi-join of the scan against the key set of `Filter(cond, T)`) under the delta condition is a semantic no-op — with `cond` and the shared table/scan correctly modeled as the same uninterpreted symbols, `SEMI` correctly standing in for the `InSubquery` filter, and the non-nullable key assumption (genuine for equality-join soundness) both encoded and disclosed. `before()` and `after()` are structurally distinct (the semi-join node is real), so the proof is non-vacuous, and the single-key-column/direct-`Filter`/WriteDelta restrictions are honest, specific PARTIAL-scope narrowings of a rule whose soundness invariant is shape-independent.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7665625
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35039416
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 886750
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 702750
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19982083
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35147083
  },
  "total_duration": {
    "secs": 0,
    "nanos": 71521125
  }
}
```
