# CombineTypedFilters

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 3  **Verification rounds used:** 1
**Scope detail:** public record CombineTypedFilters() implements RRule {


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/objects.scala, lines 78-107
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` (two nested `Filter`s with independent uninterpreted predicates `inner`/`outer` over a shared, universally quantified source) and `after()` (one `Filter` on `AND(inner, outer)`) are structurally distinct, so the proof is non-vacuous and captures exactly the rule's logical transformation — merging two nested filters into a conjunction over the same child, with `scan` standing in for the arbitrary child plan per the DSL idiom. Spark's `t1.deserializer.dataType == t2.deserializer.dataType` guard is a node-constructability constraint (the merged `TypedFilter` must have a single deserializer), not a semantic precondition: the nested-to-conjunctive-filter equivalence holds universally, and QED's quantified proof with uninterpreted (even nullable) predicates strictly subsumes the real rule's instance, so `// SCOPE: FULL` is honest.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5561125
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 31831000
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1069291
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 381708
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16468875
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 31933083
  },
  "total_duration": {
    "secs": 0,
    "nanos": 64034250
  }
}
```
