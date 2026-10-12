# RemoveRedundantAggregates

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 24  **Verification rounds used:** 2
**Scope detail:** the upper aggregate has no aggregate calls and its single group key is the lower aggregate's first group key, the lower's second group key and its aggregate call are unreferenced by the upper, and the input is a one-column scan.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/RemoveRedundantAggregates.scala, lines 30-69
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures Case 1 (`isLowerRedundant`): a group-only upper aggregate whose single group key is `lower.field(0)=k1` (a deterministic non-aggregate output) over a non-global lower aggregate, rewritten to an aggregate over `Project([k1], source)` — which matches `upper.copy(child = Project(projectList, lower.child))` with `projectList=[k1]` — and all three of `isLowerRedundant`'s preconditions (no duplicate-sensitive aggs, upper refs only deterministic non-agg, non-global lower) hold by construction, while `before()` (nested `Aggregate`) genuinely differs from `after()` (`Aggregate` over `Project`) so the proof is non-vacuous. The narrowing (one-column scan, zero upper agg calls, exactly two lower group keys plus one unreferenced agg call) is a specific, non-degenerate special case honestly flagged as PARTIAL, and full generality is genuinely out of reach because the rule's dynamic `projectList = aggregateExpressions.filter(references.contains)` and unbounded key/agg counts cannot be expressed as a fixed pattern; no symbol-sharing or missing-precondition error is present.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 11009291
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34847417
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1106875
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 715916
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 26847833
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34969458
  },
  "total_duration": {
    "secs": 0,
    "nanos": 77893375
  }
}
```
