# GenerateOptimization

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 45  **Verification rounds used:** 4
**Scope detail:** row-preserving (non-Explode, e.g. JsonTuple) generator branch with exactly 2 outputs, outer Project selects exactly 1 generator output and no child attributes.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3576-3628
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the minimal non-trivial case of the JsonTuple pruning branch: a row-preserving generate with 2 uninterpreted outputs where the outer project uses only 1, and the rewrite drops the unused output. Before/after are structurally distinct (2-col ProjectMany+1-field proj vs 1-col ProjectMany+1-field proj), the equivalence is a genuine bag-semantic fact (not vacuous), symbols out1/out2 are independent, and the SCOPE line honestly and specifically states all restrictions (2 outputs, 1 selected, no child attributes, row-preserving branch only).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
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
    "nanos": 338750
  }
}
```
