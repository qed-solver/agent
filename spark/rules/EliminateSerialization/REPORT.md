# EliminateSerialization

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** only the case where the filter's condition references solely the single deserialized-object column produced by the same uninterpreted projection used in the project (no serialization round-trip elimination, no object-append cases)


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/objects.scala, lines 40-72
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully abstracts the rule's `DeserializeToObject`-over-`TypedFilter` case: the scan is the serialized child, the uninterpreted projection `deser` stands in for deserialization, and the shared uninterpreted predicate `cond` on the deserialized column stands in for the typed filter's condition, yielding structurally distinct plans — `Project(Filter(Row, cond(deser)))` vs `Filter(Project(Row, deser), cond)` — whose bag-semantic equivalence is a genuine, non-vacuous theorem with correct symbol sharing and the type-equality guard enforced structurally. The `SCOPE: PARTIAL` line is honest and specific: it covers exactly one of the rule's four cases (no round-trip elimination, no `AppendColumns`, no typed-filter-through-serialize case), yet within that case the row type, object type, projection, and condition form are all left fully uninterpreted, so nothing real is hard-coded.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5425708
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35266541
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1055417
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 392083
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 15454750
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35353833
  },
  "total_duration": {
    "secs": 0,
    "nanos": 66134708
  }
}
```
