# FinishAnalysis

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 8  **Verification rounds used:** 1
**Scope detail:** the FinishAnalysis composite's bag-equivalent member rule PullOutGroupingExpressions, restricted to a two-column scan, one group key that is an uninterpreted scalar function g applied to column 0 (not a plain field) and one non-distinct aggregate f applied to column 1, with g depending only on column 0 and f only on column 1.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 347-384
```

## Independent verifier review

**Verdict:** CONFIRMED

FinishAnalysis as a whole is a composite of ~19 heterogeneous rewrites (several members — ComputeCurrentTime, InsertMapSort*, subquery/view/alias eliminations — have no bag-semantic meaning or are unexpressible in the DSL), so a full encoding is impossible; the porter instead encoded one genuine member, PullOutGroupingExpressions, in a precisely scoped, non-vacuous instance. The before/after plans are structurally different (Aggregate over a scan vs. Aggregate over a Project that materializes the complex key g(a)), share the same uninterpreted symbols g and f on both sides, use a plain bag scan with nullable types and no hidden key/NOT NULL preconditions that the real rule lacks, and the rewrite shape — complex group key pulled into a project below the aggregate with the key and aggregate argument re-referenced as projected fields — exactly matches Spark's rule. The SCOPE: PARTIAL line honestly and specifically names the restriction (two-column scan, one non-plain key, one non-distinct aggregate over disjoint columns), so the provable result is a faithful, useful special case rather than a vacuous or over-claimed one. ```

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
    "nanos": 532041
  }
}
```
