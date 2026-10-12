# FoldablePropagation

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 6  **Verification rounds used:** 1
**Scope detail:** fixed-shape single-foldable case: 1-column scan, Project([x, F]) with F a 0-arity (foldable) uninterpreted expression, and a parent Filter predicate referencing the alias column; models only the core constant-substitution step (no Aggregate/Join/Expand/streaming propagation).


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 1110-1241
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the core constant-substitution step of Spark's FoldablePropagation: a Project introduces a 0-arity (foldable) uninterpreted expression as its second column, and a parent Filter's reference to that output column is replaced by the foldable expression directly — the before/after differ structurally (project-output reference vs. inlined 0-arity expr) and QED's non-zero SMT/unify work confirms it verified genuine equivalence, not identity. All symbols (scan type, foldable operator, predicate) are properly uninterpreted, the 0-arity Proj correctly encodes "foldable = independent of input," and the SCOPE: PARTIAL tag honestly and specifically delimits the restrictions (single Filter parent, 1-column scan, single foldable) rather than hiding them. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5708916
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35888958
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1027875
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 422333
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16348833
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35974250
  },
  "total_duration": {
    "secs": 0,
    "nanos": 67739167
  }
}
```
