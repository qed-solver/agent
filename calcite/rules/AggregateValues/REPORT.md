# AggregateValues

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 10  **Verification rounds used:** 2
**Scope detail:** only the dedup branch (no-aggregate-call GROUP BY over all columns ≡ DISTINCT); the empty-Values grand-total branch requires aggregate-function algebra and empty-input semantics that QED does not model.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateValuesRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's dedup branch — a simple aggregate (group by the relation's only column, zero aggregate calls) over an uninterpreted scan is proven equivalent to its distinct-union (i.e. DISTINCT), and the two sides are structurally different, so the proof is non-vacuous. This is the genuinely provable half of the Calcite rule; the only unmodeled branch (empty-Values grand total) is correctly excluded in the PARTIAL scope because it requires aggregate-function algebra and empty-input semantics that QED does not model.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8441001
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 26508375
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 926042
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 367875
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21817583
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 26591791
  },
  "total_duration": {
    "secs": 0,
    "nanos": 64139500
  }
}
```
