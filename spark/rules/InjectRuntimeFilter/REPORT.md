# InjectRuntimeFilter

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 43  **Verification rounds used:** 3
**Scope detail:** single equi-key INNER join; bloom filter modeled as exact semi-join (no false positives)


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/InjectRuntimeFilter.scala, lines 38-466
```

## Independent verifier review

**Verdict:** CONFIRMED

before() = L ⋈ R and after() = (L SEMI-join R) ⋈ R are structurally different and the equivalence is a genuine non-vacuous theorem (semi-join redundancy), which is exactly the core soundness lemma of Spark's rule: filtering the application side by the creation side's key set drops no join-matching row, with the sides and key correspondence matching the source (filter on left built from right's keys). The modeling choices are correct rather than accidental — L and R are independent scans, EQUALS is appropriate since the source rule operates on equi-join keys, and even predicate co-identification would not make the proof vacuous since the identity holds for an arbitrary condition. The narrowing to a single equi-key INNER join and the idealization of the bloom filter as an exact semi-join (no false positives) are genuine, specific restrictions — false-positive semantics are physically inexpressible in the DSL — and both are explicitly and accurately disclosed in the SCOPE line, making this a faithful, honest PARTIAL encoding of the rule's proof obligation. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 10004501
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35937333
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1053167
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 614000
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 23526625
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36060416
  },
  "total_duration": {
    "secs": 0,
    "nanos": 75529750
  }
}
```
