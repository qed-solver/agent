# ColumnPruning

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 6  **Verification rounds used:** 1
**Scope detail:** Only the Project-over-Project case is covered: the inner Project's output is pruned to exactly the columns the outer Project references (here columns 0 and 2 of 4); Spark's other pruning cases (Aggregate/Expand/Union/Window/LeftExistenceJoin/etc.) are not encoded.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 1493-1638
```

## Independent verifier review

**Verdict:** CONFIRMED

before() and after() are genuinely structurally different (the inner Project emits 4 uninterpreted columns vs. 2), and the symbol sharing is exactly right — the pruned inner reuses p2_c0/p2_c2 and the outer reuses g0/g1 on the same underlying columns (field 0 and field 2 before; fields 0 and 1 after) — so the proof establishes the real pruning equivalence, not a vacuous identity or an over-constrained coincidence. The fixed shape (4 inner columns, outer referencing positions 0 and 2) is unavoidable in this pattern DSL, while the column expressions and outer projections remain uninterpreted, giving maximal expressible generality for this case; no preconditions are missing since Spark's guard (!outputSet.subsetOf(references)) is satisfied structurally. The SCOPE: PARTIAL line is honest and specific — it names exactly what is covered (Project-over-Project, cols 0 and 2 of 4) and what is omitted (Aggregate/Expand/Union/Window/LeftExistenceJoin cases), and the result is a non-degenerate, useful rule within that stated scope. ```

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
    "nanos": 101583
  }
}
```
