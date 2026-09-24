# JoinAggregateTranspose

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 36  **Verification rounds used:** 2
**Scope detail:** right side is a single-column table unique on its column.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/JoinAggregateTransposeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding mirrors the source rule's exact plan shape — Aggregate(below an INNER join) ⟹ INNER join under an Aggregate whose group set is extended with all right-side columns, followed by a project restoring the original column order — and it shares the same uninterpreted join predicate and the same uninterpreted aggregate symbol across both sides, with the condition referencing only the group key on the aggregate side (the precondition that makes the transpose sound). The two load-bearing preconditions of the original rule are correctly captured: the right input is declared unique on its join key via the `unique` scan flag (mirroring `mq.areColumnsUnique(right, info.rightSet())`), and the group set is non-empty by construction (so the empty-group/COUNT(*) caveat is moot); the proof is non-vacuous because it genuinely requires QED to relate the join-condition's group key through the aggregate on the before side. The stated PARTIAL scope (single-column, unique right table) is a real DSL-forced restriction (keyed multi-column scans are not exposed), and the implicit left-side minimalization to one group column plus one SUM call over the other column adds no new semantic content to the obligation, making this a faithful, non-degenerate special case rather than a mis-encoded or over-constrained coincidence. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 11204250
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 47805042
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 970917
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 860708
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 27562291
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 48096333
  },
  "total_duration": {
    "secs": 0,
    "nanos": 92112708
  }
}
```
