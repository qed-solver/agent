# FilterFlattenCorrelatedCondition

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 34  **Verification rounds used:** 2
**Scope detail:** a single (uninterpreted) comparison between a correlated outer column and one uncorrelated computed expression over inner columns; the correlation context is modeled as an INNER cross-join of the outer scan and the inner scan, and the hoisted expression may not reference the outer columns


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterFlattenCorrelatedConditionRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

It proves a non-vacuous, honestly scoped special case of the Calcite rewrite: one uninterpreted comparison between an outer column and an uninterpreted inner expression is transformed by hoisting that expression into a projection and referencing it by input ref. The shared Cmp/Expr symbols and the project/filter/project-back shape match the source rule’s core transformation, while the cross join models the correlated context because the DSL lacks a correlate builder.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8577917
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 37939083
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 874792
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 529166
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 23704292
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 38065000
  },
  "total_duration": {
    "secs": 0,
    "nanos": 77974916
  }
}
```
