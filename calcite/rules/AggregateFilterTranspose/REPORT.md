# AggregateFilterTranspose

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 9  **Verification rounds used:** 1
**Scope detail:** Case 1 of the rule (Group.SIMPLE, filter columns all in the group set): the aggregate has exactly one group key which is a plain field of a two-column input, one non-distinct aggregate call over the other field, and the filter predicate depends only on the group key.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateFilterTransposeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures Case 1 of the source rule: the uninterpreted predicate depends only on the group key (i.e. all filter columns are in the group set), the aggregate is a simple group-by with the same group set and agg call on both sides, and the filter genuinely moves from below to above the aggregate, so the proof is non-vacuous and matches the rule's transformation exactly. The symbols are shared correctly — the same `p` and `f` occur in both patterns, and the after-side filter references the aggregate's group-key output column, which is precisely what the rule's column mapping does when the group set is unchanged; the source rule's uniqueness check is a termination/rule-firing guard, not a semantic precondition, so its absence doesn't weaken the equivalence claim. The stated PARTIAL scope (one group key, two-column input, one non-distinct call) is honest, specific, and non-degenerate: fixed-shape patterns with uninterpreted symbols are all this DSL can express, and the essential semantic condition (predicate over group keys only) is captured, so the narrower arity is an inherent language limitation rather than a hard-coded concrete value.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 13136166
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 46110209
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 973542
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1012917
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 30353917
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 46360250
  },
  "total_duration": {
    "secs": 0,
    "nanos": 93033833
  }
}
```
