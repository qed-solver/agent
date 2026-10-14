# LoptOptimizeJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 0  **Verification rounds used:** 0
**Scope detail:** all-inner 3-way MultiJoin(A,B,C) with combined condition pAB(A,B) ∧ pAC(A,C), one valid heuristic output: reorder (A⋈B)⋈C to (A⋈C)⋈B, push pAC into the A⋈C join, keep pAB at the root, restore the (A,B,C) column order with a top project; the cost model, outer-join removal, semijoin and self-join handling of the general rule are not modeled


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/LoptOptimizeJoinRule.java
```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6666501
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 6215250
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 79958
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 539542
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 13634208
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 6263167
  },
  "total_duration": {
    "secs": 0,
    "nanos": 22550208
  }
}
```
