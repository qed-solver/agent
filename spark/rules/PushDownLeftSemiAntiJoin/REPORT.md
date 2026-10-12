# PushDownLeftSemiAntiJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 8  **Verification rounds used:** 1
**Scope detail:** only the LeftSemi/over-Project case of the rule (Spark also fires on LeftAnti and on Aggregate/Window/Union/other unary operators); left projection is an arbitrary multi-expression project and the join condition an uninterpreted predicate over the projected row


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PushDownLeftSemiAntiJoin.scala, lines 35-172
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous (before = SemiJoin(Project(X),Y) vs after = Project(SemiJoin(X,Y))) and semantically correct: because P0/P1 are shared uninterpreted (hence functional) symbols and C is shared, the condition inlines identically on both sides, which mirrors Spark's determinism precondition, and under bag-semijoin semantics both plans yield the same left-leg rows. It is a genuine, non-degenerate special case—LeftSemi only (not LeftAnti) and the over-Project case only (not Aggregate/Window/Union/Unary), with self-joins excluded by using two distinct scans—and the SCOPE: PARTIAL line honestly and specifically discloses exactly those restrictions, so the provable result is meaningful for what it covers.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8535291
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36044208
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1071666
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 636292
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21271083
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36154334
  },
  "total_duration": {
    "secs": 0,
    "nanos": 73494459
  }
}
```
