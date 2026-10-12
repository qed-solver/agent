# OptimizeUpdateFields

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 64  **Verification rounds used:** 4
**Scope detail:** 2-field struct with exactly three WithField operations in which field a is updated twice and field b once (no DeleteField, case-sensitive names), each value expression uninterpreted and evaluated against the original struct row.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/UpdateFields.scala, lines 33-77
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully models the source rule's dedup case for its declared instance: each WithField is denoted by a projection whose value expression (distinct uninterpreted ops E1/E2/E3) is evaluated against the carried original row, matching Spark's expression semantics (all ops evaluate against the input row, applied in order, last write wins), and the after() list [WF(a,E3), WF(b,E2)] exactly matches the rule's "last value per name, first-occurrence order" reconstruction. before() and after() are structurally different plans — before() carries the dead E1(a,b) computation that after() eliminates — so the proof is not vacuous; it genuinely verifies that an overwritten WithField can be dropped. The 2-field/3-operation fixed shape and all-WithField restriction are inherent to this fixed-shape pattern language (no unbounded arity or DeleteField/case-folding modeling possible), are honestly and specifically declared in the PARTIAL scope line, and the instance is non-degenerate rather than trivially identical. ```

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
    "nanos": 321083
  }
}
```
