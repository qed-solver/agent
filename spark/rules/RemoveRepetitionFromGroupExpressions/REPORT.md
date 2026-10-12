# RemoveRepetitionFromGroupExpressions

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 8  **Verification rounds used:** 3
**Scope detail:** group keys are plain input columns with the first key duplicated as the third, and a single aggregate call.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3634-3645
```

## Independent verifier review

**Verdict:** CONFIRMED

The proof is non-vacuous: before() is a 3-key aggregate over a projection (with a trailing projection dropping the duplicate key) while after() is a 2-key aggregate directly on the scan, so QED verified a real equivalence, not a structural tautology. The duplicate group key being encoded as literally the same underlying column (s2.field(2) ≡ s.field(0)) is the maximal generality QED can certify, since the source rule removes only *identical* grouping expressions and QED cannot prove semantic equality of two distinct uninterpreted projections. The remaining narrowing (single uninterpreted aggregate call, "sum", on the non-key column) is specific and honestly declared in the PARTIAL scope tag, the aggregate operand maps to the same base column on both sides, and no preconditions from the source rule are missing. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 10312583
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 44796625
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1212959
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 830500
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 25434666
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 44999750
  },
  "total_duration": {
    "secs": 0,
    "nanos": 86584125
  }
}
```
