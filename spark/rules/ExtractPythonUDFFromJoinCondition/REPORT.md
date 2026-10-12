# ExtractPythonUDFFromJoinCondition

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** public record ExtractPythonUDFFromJoinCondition() implements RRule {


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/joins.scala, lines 245-282
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-trivial (before is an INNER join with conjunctive condition `and(udf, rest)`; after is an INNER join with condition `rest` plus a top filter `udf`), correctly uses INNER to stand in for the source's InnerLike-only scope, and reuses the same uninterpreted `udf`/`rest` symbols on both sides, so QED verifies the genuine bag-semantic filter-pullup identity for all instantiations rather than a vacuous or over-constrained claim; the minor deviations (INNER rather than Cross, predicates expressed over left's columns, `rest` always present as a real predicate) do not alter the universally valid identity and faithfully reflect the rule's effective effect.

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
    "nanos": 1108291
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 445500
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
    "nanos": 1906834
  }
}
```
