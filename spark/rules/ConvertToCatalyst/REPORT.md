# ConvertToCatalyst

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 24  **Verification rounds used:** 2
**Scope detail:** one shared subexpression e read twice by a single predicate F under a Filter; the widened column is projected back to keep the output width


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 1035-1411
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the core transformation of `ConvertToCatalyst`'s `convertOperator` with-columns path: materializing a shared subexpression `E(x)` as a Project column so the predicate reads it twice from a column instead of re-evaluating it twice inline. The before (`Filter(F(E(x),E(x)), S)`) and after (`Project(x, Filter(F(t,t), Project(x,E(x),S)))`) are structurally distinct, the same uninterpreted symbols `eOp`/`fOp` are correctly shared across both sides, and the SCOPE:PARTIAL line accurately names the genuine restriction (single Filter, one shared subexpression, two reads) versus the full rule's generality (arbitrary single-child operators, multiple shared expressions, non-deterministic handling, lambda/command/aggregate exclusions).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5722583
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36422875
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1104625
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 451500
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16167375
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36505125
  },
  "total_duration": {
    "secs": 0,
    "nanos": 68514666
  }
}
```
