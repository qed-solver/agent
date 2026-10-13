# OptimizeSubqueries

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 50  **Verification rounds used:** 3
**Scope detail:** only the projection-insertion step of the Exists case: prune subquery output to the columns referenced in the EXISTS join condition


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 389-490
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the Exists case's projection step: before() is a SEMI join of L with a 3-column right side R where the (uninterpreted) join condition references only R's columns 0 and 2, and after() replaces R with R.project(columns 0,2) and adjusts the condition's field ordinals accordingly — the JoinField arithmetic checks out (L.joinField(3,R) → R.col2 maps to L.joinField(2,R2) → R2.col1 = R.col2), the same uninterpreted predicate symbol is shared correctly, and the structural difference between before and after is genuine and non-vacuous. The SCOPE: PARTIAL tag is honest: the full Spark rule has three subcases (COUNT-bug scalar subquery, Exists, generic subquery) plus top-level-sort removal, and this encodes only the Exists column-pruning step, which is a specific, non-degenerate slice of the real optimization. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8763460
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35477500
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1107333
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 562000
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 22069959
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35589750
  },
  "total_duration": {
    "secs": 0,
    "nanos": 73769958
  }
}
```
