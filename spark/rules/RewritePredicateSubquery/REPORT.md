# RewritePredicateSubquery

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 0  **Verification rounds used:** 0
**Scope detail:** the correlated `Filter(EXISTS(sub))` -> LEFT SEMI join branch on a single equality correlation key over single-column relations; the NOT EXISTS / IN / NOT IN (null-aware) branches, multi-key conditions and domain-join handling are not encoded


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/subquery.scala, lines 56-457
```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9727250
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34601292
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1126583
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 508459
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 22992750
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34694000
  },
  "total_duration": {
    "secs": 0,
    "nanos": 73593792
  }
}
```
