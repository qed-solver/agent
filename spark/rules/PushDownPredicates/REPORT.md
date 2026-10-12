# PushDownPredicates

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** INNER join only: a single left-only parent-filter conjunct is pushed below the join, leaving the existing join condition unchanged (right-only/common conjuncts and all other dispatcher cases are omitted).


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2547-2554
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-vacuous fragment of Spark's dispatcher: `Filter(left_only, L INNER join R on join_cond)` vs. `L filter left_only INNER join R on join_cond` are structurally distinct shapes, and the proof is meaningful precisely because the *same* uninterpreted predicate `left_only` is shared on the same L column in both positions — the pushdown is sound only for the identical predicate, so the required symbol sharing is correct rather than an over-constraining coincidence (had the two predicate occurrences been independent symbols the rewrite would be unsound and QED would have reported not-provable), while `join_cond` remains an arbitrary uninterpreted binary predicate over both inputs. The PARTIAL tag is honest and specific — INNER join only, a single left-only parent conjunct, with right-only/common conjuncts and the dispatcher's other branches (outer-join cases, CombineFilters, non-join pushdown) explicitly omitted — and pushing a left-only filter below an inner join is a genuine, high-value, non-degenerate sub-case of the source rule.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6731917
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34325208
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1099292
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 521417
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18410833
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34420625
  },
  "total_duration": {
    "secs": 0,
    "nanos": 68906667
  }
}
```
