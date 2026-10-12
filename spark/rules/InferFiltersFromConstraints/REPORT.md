# InferFiltersFromConstraints

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** inner joins only, over two one-column scans with no pre-existing constraints, whose join condition is an equality between the left key and the right key plus one conjunct over the left key only; the rule adds the inferred left filter (left-only conjunct AND left key IS NOT NULL) and right filter (right key IS NOT NULL), leaving the join condition unchanged


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2238-2309
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a non-trivial, honest PARTIAL special case of Spark's `InferFiltersFromConstraints`: it correctly models the core mechanism (equality `L=R` being null-rejecting, so `IS NOT NULL` is inferred on both sides, and a left-only conjunct `g(L)` is pushed as a filter below the join) with proper symbol sharing (`g` appears in both the join condition and the left filter; `left.field(0)` and `left.joinField(0, right)` denote the same column). The before/after are structurally distinct and the equivalence is non-vacuous (the after adds explicit filters that the before implicitly enforces via the null-rejecting join condition). The SCOPE line accurately enumerates the restrictions (inner join only, single-column scans, no pre-existing constraints, fixed condition shape), and none of the failure modes apply.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6289334
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 8127542
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 68625
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 439916
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 12659625
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 8181625
  },
  "total_duration": {
    "secs": 0,
    "nanos": 23729375
  }
}
```
