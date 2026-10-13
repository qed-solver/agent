# IntersectToExists

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 31  **Verification rounds used:** 2
**Scope detail:** the 2-input, single-column instance in which both inputs share one row type, so the rule's type-unification casts are identity (the general n-way rule is obtained by repeated application of this binary step)


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/IntersectToExistsRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully mirrors the rule's actual transformation — INTERSECT with all=false ⟹ a SEMI-correlate (the decorrelated EXISTS filter) on IS NOT DISTINCT FROM over the whole row, followed by the rule's final DISTINCT as a group-all/no-aggregate-calls Aggregate — and before() (an INTERSECT node) vs. after() (AGGREGATE over CORRELATE) differ structurally, so the proof is non-vacuous; the concrete IS_NOT_DISTINCT_FROM operator and SEMI kind are exactly what the source rule constructs (an uninterpreted predicate or EQ would have been the unfaithful choice), A and B remain independent non-unique scans, and no hidden preconditions (PK/NOT NULL) are assumed since the rule needs none and nulls are handled by INDF on both sides. The PARTIAL scope is honest and specific — 2 inputs, single column, shared row type so the rule's defensive type-unification casts are identity, with the n-way case obtained compositionally — and it is a genuine, non-degenerate fragment of the real rule rather than a narrowed-to-identical trick.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 10728667
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 27373125
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1110833
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 654792
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 26946041
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 27526875
  },
  "total_duration": {
    "secs": 0,
    "nanos": 71712292
  }
}
```
