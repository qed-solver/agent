# EliminateMapObjects

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** the identity projection is over a fixed single-column input; the original rule applies to any input arity (and, like the source, assumes non-nullable primitive types)


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/objects.scala, lines 115-120
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is faithful: `MapObjects(λv. v, input, None)` is semantically an identity transformation on its input, and the porter models it as a concrete identity projection (`project(field(0))`) over an uninterpreted input relation — the correct relational analog, structurally distinct from the bare `after()`, so the proof is non-vacuous; the `serializedColumns = None` premise is reflected (no extra columns) and the `forceNullable = false` premise is mirrored in the non-nullable type, which does not weaken the equivalence. The only restriction is fixed single-column arity, where the original admits any arity — that is genuinely inexpressible in the DSL (no arity-polymorphic patterns; schemas are fixed-arity), is honestly and specifically declared in the SCOPE line, and the result remains a non-degenerate, true instance of the rule's content.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 0
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 0
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 0
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 0
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 0
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 325542
  }
}
```
