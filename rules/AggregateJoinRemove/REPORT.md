# AggregateJoinRemove

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 67  **Verification rounds used:** 3
**Scope detail:** the join is a LEFT join of two one-column scans, and the aggregate over it has exactly one group key referencing the left (preserved) input's column and no aggregate calls.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateJoinRemoveRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding correctly captures the core semantic content of AggregateJoinRemove's LEFT-join branch: a DISTINCT aggregate (group key on the preserved side only, no non-preserved column references, vacuously-satisfied all-distinct condition) over a LEFT JOIN is equivalent to the same aggregate over the left input alone. The before/after are structurally different (join present vs. absent), the proof is non-vacuous (requires reasoning about left-join row preservation combined with group-by idempotence), the join condition is properly uninterpreted, symbol sharing is correct (left appears in both sides as the same table, right only in before), and no spurious preconditions (keys, NOT NULL) are introduced. The scope line honestly and specifically enumerates every restriction (LEFT-only, one-column scans, single group key, no aggregate calls); the one-column limitation is a genuine DSL constraint (the Scan constructor always emits a single-column table), the LEFT-only choice is inherent to the single before/after format, and the no-agg-calls restriction reflects QED's inability to reason about DISTINCT aggregate algebra — so the narrowing is to a provable core rather than an avoidable omission. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 11234960
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34074333
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1524750
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 653709
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 26591250
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34184125
  },
  "total_duration": {
    "secs": 0,
    "nanos": 77373667
  }
}
```
