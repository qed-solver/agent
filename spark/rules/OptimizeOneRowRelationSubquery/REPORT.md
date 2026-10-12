# OptimizeOneRowRelationSubquery

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 48  **Verification rounds used:** 3
**Scope detail:** only the uncorrelated single-column scalar-subquery branch is encoded, as the inner cross-join of the outer relation with a concrete one-row constant-valued VALUES relation inlined as that constant in the outer project


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/subquery.scala, lines 1104-1167
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the uncorrelated scalar-subquery branch's relational-algebra shape (inner cross-join with a one-row relation → inlined constant in the outer project), is structurally non-vacuous (before has a Join node that after eliminates), uses the correct INNER join kind with a true condition matching Spark's scalar-subquery decorrelation, and its narrowing to a constant-valued VALUES relation is a genuine DSL limitation (no cardinality constraint on uninterpreted scans, so an arbitrary one-row relation with an unknown value cannot be expressed); the SCOPE tag honestly and specifically states this restriction.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5470126
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35182417
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1131542
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 340708
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 15520834
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35249334
  },
  "total_duration": {
    "secs": 0,
    "nanos": 66445625
  }
}
```
