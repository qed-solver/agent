# PushFilterIntoAggregate

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 6  **Verification rounds used:** 1
**Scope detail:** the aggregate has exactly one group key (a plain input column) and one non-distinct aggregate call, and the filter predicate depends only on the group key.


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_filter.rs, lines 1012-1067
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` (Filter over Aggregate) and `after()` (Aggregate over Filter) are genuinely different plan shapes, so the proof is non-vacuous, and the single uninterpreted predicate "p" is correctly shared on the same group-key column across both sides — exactly mirroring the rule moving a key-only predicate through the aggregate, with the key column properly linked via the group set. The bag semantics genuinely match (for a key-dependent filter, groups are dropped/kept identically on both sides), so no missing precondition changes the claim. The SCOPE line honestly documents the real narrowing (one plain-column group key, one non-distinct call, whole predicate key-dependent) rather than vague hand-waving, and this remains a useful, non-degenerate special case of the core rewrite.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 10244916
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 51754875
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 979708
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 763542
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 25082625
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 52029791
  },
  "total_duration": {
    "secs": 0,
    "nanos": 92967833
  }
}
```
