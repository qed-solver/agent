# IntersectToSemiJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 8  **Verification rounds used:** 1
**Scope detail:** the 2-input, single-column instance in which both inputs share one row type, so the rule's type-unification casts are identity (the general n-way rule is obtained by repeated application of this binary step).


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/IntersectToSemiJoinRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The pattern is non-vacuous and matches the source rule's binary step: set INTERSECT over independent uninterpreted A and B is rewritten to A SEMI-JOIN B on IS NOT DISTINCT FROM followed by a group-by-only distinct, with the correct all=false, SEMI, and final-duplicate-elimination shapes. The stated PARTIAL scope is honest because the full Calcite rule additionally handles n-way inputs, multi-column conjunctions, and least-type casts, but this single-column same-type case is a genuine non-degenerate special case.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9962584
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 25852000
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 905792
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 564292
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 25266917
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 25952250
  },
  "total_duration": {
    "secs": 0,
    "nanos": 67415625
  }
}
```
