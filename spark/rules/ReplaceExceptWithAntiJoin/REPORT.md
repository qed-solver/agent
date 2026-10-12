# ReplaceExceptWithAntiJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 11  **Verification rounds used:** 2
**Scope detail:** 2-column instance; both EXCEPT operands share one row type as Spark's analyzer guarantees


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3404-3412
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully models Spark's rule for a 2-column instance: `a.minus(false,b)` is the set-difference `EXCEPT` (matching `Except(left,right,false)`), and the after-side is a `DISTINCT` (group-by on all output columns, no agg calls) over a left-anti join (`JoinRelType.ANTI` = Spark's `LeftAnti`) whose condition is the AND of per-column null-safe equalities built from `SqlStdOperatorTable.IS_NOT_DISTINCT_FROM` — exactly Spark's `EqualNullSafe`, which is essential because `EXCEPT`'s row equality is null-safe. `A` and `B` are independent scan symbols ( [NOTE: response was truncated at the token limit before finishing — if this cut off mid-code-block, that's why it couldn't be parsed.]

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8837043
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 6065625
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 61334
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 481458
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 17406833
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 6106667
  },
  "total_duration": {
    "secs": 0,
    "nanos": 26441000
  }
}
```
