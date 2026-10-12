# PruneFileSourcePartitions

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 11  **Verification rounds used:** 1
**Scope detail:** the partition-pruned file index is modeled as exactly the rows of the original scan matching the partition-key filters; Spark's real pruned index may additionally retain partitions it could not evaluate (a superset whose extra rows are eliminated by the kept full filter), a looser containment between scans the DSL cannot express.


## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/PruneFileSourcePartitions.scala, lines 35-98
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` (Project(π, Filter(P∧D, R))) and `after()` (Project(π, Filter(P∧D, Filter(P, R)))) are structurally different, so the proof is non-vacuous, and P/D/π are properly left uninterpreted with P restricted to the partition columns exactly as Spark's `getPartitionFiltersAndDataFilters` requires, with the full filter kept on top as in `rebuildPhysicalOperation`. The single modeling choice — the pruned index being *exactly* `Filter(P, R)` rather than any intermediate superset — reflects a real QED/DSL limitation (no way to assert row-set containment between two uninterpreted scans), and it is an instance Spark genuinely produces when all partitions are statically evaluable, so the result is a real, non-degenerate special case. The `SCOPE: PARTIAL` line accurately and specifically discloses this containment gap, so the "provable" verdict is faithful and not misleading.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 3602209
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 5314834
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 55708
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 380584
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 7979250
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 5357666
  },
  "total_duration": {
    "secs": 0,
    "nanos": 15834333
  }
}
```
