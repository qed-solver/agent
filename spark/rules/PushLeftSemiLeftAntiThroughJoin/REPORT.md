# PushLeftSemiLeftAntiThroughJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 24  **Verification rounds used:** 2
**Scope detail:** LeftSemi only (not LeftAnti), TO_LEFT_BRANCH with a non-empty semi condition whose conjuncts reference only the child join's left leg and the semi-join right input, and an INNER child join, with single-column scans


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PushDownLeftSemiAntiJoin.scala, lines 190-275
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the TO_LEFT_BRANCH, INNER-child-join, LeftSemi (not anti) special case of Spark's rule: before is SEMI(INNER(X,Y,cOp), Z, sOp) with sOp referencing only X and Z fields (matching the precondition that all semi-condition conjuncts reference only childLeft∪semiRight), and after is INNER(SEMI(X,Z,sOp), Y, cOp) — a structurally different plan where the semi-join is pushed through the inner join. The uninterpreted predicates sOp and cOp are correctly shared across before/after with proper field re-indexing, no key/NOT-NULL preconditions are needed for this bag-semantic commutation, and the single-column scans don't lose generality since the predicates are opaque functions whose only constraint (which tables they reference) is already captured. The SCOPE: PARTIAL annotation honestly lists each restriction (no LeftAnti, no TO_RIGHT_BRANCH, INNER only vs. InnerLike/LeftOuter, non-empty condition only).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9997125
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 37808958
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1147416
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 660291
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 24566875
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 37927041
  },
  "total_duration": {
    "secs": 0,
    "nanos": 78432750
  }
}
```
