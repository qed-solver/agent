# TransposeWindow

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 64  **Verification rounds used:** 4
**Scope detail:** QED has no window operator, so each window is modeled as a group-by aggregate over the same source joined back to the source on the partition keys (partitions (a) and (a, b) with (a) a proper subset of (a, b), and the outer window's function depending only on a base column), and only case 1 of the rule (two directly stacked windows, no Project in between) is encoded.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2159-2185
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully models each window as a partition-keyed aggregate joined back to the shared source, correctly bakes in the key precondition (w1's aggregate is computed over the base relation, not over w2's output, matching `w1.references ∩ w2.windowOutputSet = ∅`), satisfies the partition-subset compatibility via (a) ⊂ (a,b), and uses distinct uninterpreted functions for the two windows; `before()` and `after()` genuinely differ in join order, so the proof captures the real transpose (commutation of the two window operations) rather than a vacuous identity. The narrowings (concrete (a)/(a,b) partition shape, whole-partition aggregates with no frame/ordering, and only case 1 of the two-case rule) are real but are honestly flagged in the `// SCOPE: PARTIAL` line and leave a non-degenerate, meaningful result.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 18403292
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36804083
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1183667
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1558167
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 39474375
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36970333
  },
  "total_duration": {
    "secs": 0,
    "nanos": 93054000
  }
}
```
