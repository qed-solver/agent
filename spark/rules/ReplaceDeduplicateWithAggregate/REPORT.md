# ReplaceDeduplicateWithAggregate

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 44  **Verification rounds used:** 3
**Scope detail:** the dedup keys must equal all of the child's output columns, so the rewrite reduces to DISTINCT ≡ GROUP BY all columns with no aggregate calls (no First() involved)


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3343-3368
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the genuine special case where all child output columns are dedup keys, reducing the Spark rule to DISTINCT ≡ GROUP BY-all-cols-with-no-aggregates; `intersect(false, source)` correctly models set self-intersection (DISTINCT) and the `Aggregate` with all fields as group keys and empty agg-calls correctly models the corresponding GROUP BY, so the proof is non-vacuous and semantically sound. The PARTIAL scope is honest and specific: the full rule's `First()` on non-key columns relies on aggregate semantics QED cannot reason about, making this a legitimate, non-degenerate narrowing.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7648041
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34538792
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1137959
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 419709
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19905958
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34618333
  },
  "total_duration": {
    "secs": 0,
    "nanos": 70348042
  }
}
```
