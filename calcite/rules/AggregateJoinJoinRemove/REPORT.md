# AggregateJoinJoinRemove

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 50  **Verification rounds used:** 4
**Scope detail:** one-column scans A, B, C with both joins LEFT; the top join condition is a single uninterpreted predicate over (A's column, C's column), identically instantiated on both sides (index-shifted from fields (0,2) to (0,1) when the bottom join is removed); the bottom join condition is uninterpreted over (A, B); the aggregate is a pure DISTINCT group-by on (A's column, C's column) with no aggregate calls, mirroring the rule's "select distinct s.product_id, pc.product_id" example.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateJoinJoinRemoveRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, honestly-tagged PARTIAL instance of the rule — pure DISTINCT on (A.col, C.col) over single-column inputs, exactly the rule's own doc example — and all of the rule's real preconditions hold structurally in it: the group set and top condition never reference the removed B (BR) column, both joins are LEFT (the rule's only join kinds), and the left-key sets are equal (both {A.col}), with B's uniqueness correctly NOT assumed (plain non-unique scans). Symbol sharing is correct rather than coincidentally over-constraining: top_cond is deliberately shared between before/after with the same index remapping (0,2)→(0,1) that the source rule performs via RexUtil.shift, while bottom_cond remains an independent uninterpreted predicate exactly as the source rule treats it (only its referenced left columns are constrained, its predicate itself is not). before() genuinely contains the extra A ⋈_LEFT B join that after() eliminates, so the QED proof is non-vacuous and establishes the rule's actual claim — distinct(A,C) over (A⋈B)⋈C equals distinct(A,C) over A⋈C under independent uninterpreted conditions — for this non-degenerate fragment, with the narrowing (no aggregate calls, one column per table) specifically and truthfully stated in the SCOPE line. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 28751374
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36671917
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 994291
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1940875
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 61017375
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36885042
  },
  "total_duration": {
    "secs": 0,
    "nanos": 115958208
  }
}
```
