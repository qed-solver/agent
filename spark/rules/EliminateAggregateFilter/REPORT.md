# EliminateAggregateFilter

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 30  **Verification rounds used:** 3
**Scope detail:** only the True-literal arm, i.e. every aggregate call in the node has FILTER (WHERE true), which is equivalent to filtering the aggregate input by the constant true; the False-literal arm (replacement by the empty/identity aggregate value) is out of reach for uninterpreted aggregates.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 635-651
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` and `after()` differ exactly by the constant-true filter feeding the aggregate, which is the precise semantic content of the rule's True-literal arm (FILTER (WHERE true) is a no-op), and since the DSL's `AggCall` has no per-call filter, modeling it as an input-level `Filter(trueLiteral)` is the faithful representation of that arm. The scan, its types, the group key/value fields, and the uninterpreted aggregate `f` are all shared correctly between the two sides (same table, same `f`, same grouping), so the proof is not vacuous and must go through the group-by/aggregate structure rather than relying on a coincidental symbol identity. The `PARTIAL` tag is honest and specific — the False-literal arm requires identity/empty-value algebra for specific aggregate functions, which QED's uninterpreted aggregates deliberately don't model — and the minimal one-key/one-call/one-operand shape loses no proof content, since the true-filter-identity argument is arity-independent.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
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
    "nanos": 0
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 0
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
    "nanos": 516125
  }
}
```
