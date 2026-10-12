# OptimizeJoinCondition

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 26  **Verification rounds used:** 2
**Scope detail:** the pattern is the entire condition of an inner join between two single-column scans of a shared type, rather than a (nested) subterm of the condition of a join of any kind


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/OptimizeJoinCondition.scala, lines 29-65
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding proves exactly the rule's semantic core — `x = y OR (x IS NULL AND y IS NULL)` ≡ `x IS NOT DISTINCT FROM y` (Spark's `EqualNullSafe`) — with the correct operators, and correctly embedded in a join where only TRUE passes, so the NULL-vs-FALSE discrepancy in the mixed-null case is behaviorally irrelevant and the equivalence genuinely holds. `before()` and `after()` are structurally different and the two-column/two-scan symbols (`x` on `L`, `y` on `R`) are independent, so the proof is non-vacuous rather than a symbol-sharing accident. The narrowings to an INNER join, the whole condition (no And/Or subterm recursion), and single-column same-type scans are genuine, specific, and honestly declared in the `PARTIAL` scope line rather than silently baked in, making this a faithful non-trivial special case of the source rule.

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
    "nanos": 378625
  }
}
```
