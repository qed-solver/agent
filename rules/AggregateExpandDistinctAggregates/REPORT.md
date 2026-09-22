# AggregateExpandDistinctAggregates

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 72  **Verification rounds used:** 4
**Scope detail:** all distinct aggregate calls share the same single argument, no non-distinct calls, no FILTER/GROUPING SETS/WITHIN GROUP


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateExpandDistinctAggregatesRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully mirrors the source rule's `convertMonopole` branch: `before()` is `GROUP BY k` of two distinct calls `f(DISTINCT x), g(DISTINCT x)`, and `after()` makes them non-distinct on top of an intermediate `GROUP BY k, x` (no calls) that is exactly the source's `SELECT DISTINCT`-via-aggregate ded [NOTE: response was truncated at the token limit before finishing — if this cut off mid-code-block, that's why it couldn't be parsed.]

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 17834793
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 57600124
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 993208
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1285250
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 39401833
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 58029375
  },
  "total_duration": {
    "secs": 0,
    "nanos": 114404667
  }
}
```
