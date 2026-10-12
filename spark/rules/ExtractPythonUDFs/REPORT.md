# ExtractPythonUDFs

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 10  **Verification rounds used:** 1
**Scope detail:** single-column input and one scalar deterministic Python UDF f applied to that column, extracted out of a Project operator into a dedicated EvalPython projection below it (the operator keeps a non-Python expression g on the input column).


## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/python/ExtractPythonUDFs.scala, lines 162-396
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the core semantic transformation of ExtractPythonUDFs for its declared partial scope: a UDF computation f(x) is extracted from an upper Project into a dedicated lower node (modeled as a Project that computes f and passes through x), and the upper node references the pre-computed column instead of recomputing. The before/after plans are structurally distinct (one Project vs. Project-over-Project), so the proof is non-vacuous; f and g are properly uninterpreted projection operators (not hard-coded predicates or constants); and the SCOPE:PARTIAL tag honestly and specifically declares the single-column/single-UDF/single-expression restriction, which is a legitimate simplest non-degenerate case of the general rule.

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
    "nanos": 439000
  }
}
```
