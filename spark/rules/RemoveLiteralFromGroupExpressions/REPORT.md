# RemoveLiteralFromGroupExpressions

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 63  **Verification rounds used:** 6
**Scope detail:** one constant group key, one column group key, two arbitrary non-distinct aggregates carried through unchanged.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3556-3570
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-trivial and faithful to the rule's core transformation: it removes a constant (uninterpreted 0-ary projection `c`) from the group-by keys of an Aggregate and re-introduces it via a ProjectMany, which is exactly what the Spark rule does for the case where at least one non-literal key remains. The PARTIAL scope is honestly and specifically stated (one literal key, one column key, two fixed aggregates), the symbol sharing is correct (same `c`, `f`, `g` in both sides), and no precondition is silently dropped for this instance.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 13447666
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 58262084
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1159750
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1035708
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 31322209
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 58577792
  },
  "total_duration": {
    "secs": 0,
    "nanos": 106266042
  }
}
```
