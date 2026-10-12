# ReorderJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 26  **Verification rounds used:** 2
**Scope detail:** a single concrete 4-way inner-join reordering (left-deep (A,D,C,B) input re-jointed as ((A x B) x D) x C) with fixed uninterpreted conditions pAB, pCD, qDBC, single-column scans, and the trailing output-order project, rather than the general condition-partitioning / star-schema / CBO logic


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/joins.scala, lines 45-122
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully traces Spark's createOrderedJoin algorithm for a specific 4-way input (A,D,C,B) with conditions {pAB, pCD, qDBC}: the algorithm correctly selects B first (pAB is the only condition referencing both A and B), then D (no remaining condition is subset of {A,B}∪{D}), then C with the residual conditions — exactly matching the porter's after() tree. All field indices are verified correct (e.g., qDBC_before = qDBC(D=1, B=3, C=2) in the (A,D,C,B) layout and qDBC_after = qDBC(D=2, B=1, C=3) in the (A,B,D,C) layout apply the same uninterpreted symbol to the same logical columns), the cross-join-with-true-condition correctly models Spark's Cross/Inner-with-no-condition, the trailing project [0,2,3,1] matches the rule's sameOutput/Project injection, and the SCOPE: PARTIAL line accurately and specifically delimits the narrowness (one concrete 4-way topology, single-column scans) while keeping the predicates uninterpreted so the bag-equivalence proof is genuinely universal over all predicate instantiations. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9734540
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34269709
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1184459
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 769459
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 24030500
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34370417
  },
  "total_duration": {
    "secs": 0,
    "nanos": 74281625
  }
}
```
