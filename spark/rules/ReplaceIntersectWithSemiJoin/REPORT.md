# ReplaceIntersectWithSemiJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 25  **Verification rounds used:** 2
**Scope detail:** the 2-input, 2-column instance in which both inputs share one row type; the general n-column rule is the same construction with a longer conjunction of null-safe equalities.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3382-3390
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully mirrors every operator choice in the rule — `intersect(false)` for INTERSECT DISTINCT, `JoinRelType.SEMI` for LeftSemi, `IS_NOT_DISTINCT_FROM` for Spark's `EqualNullSafe`, and the `Distinct`-as-group-by-all `Aggregate` over the semi-join's left columns — with `a` and `b` as independent same-schema scans and correctly paired per-column null-safe join fields, so `before` (set intersection) and `after` (semi-join + distinct) are genuinely different shapes QED had to relate (a non-vacuous proof, since the identity depends on the interpreted null-safe equality). The sole narrowing is a fixed 2-column arity versus the rule's parametric n, which is inherent to the DSL's fixed-shape patterns (arity can't be a symbol), is called out in the SCOPE line, and is non-degenerate since the construction is identical for any n.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9409458
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 24805083
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1100709
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 535209
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 23300625
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 24884042
  },
  "total_duration": {
    "secs": 0,
    "nanos": 63866625
  }
}
```
