# ObjectSerializerPruning

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** Covers the core individual-serializer pruning case of Spark's ObjectSerializerPruning (Project over SerializeFromObject): the serializer (3 uninterpreted fields over the single input object column, here fields 0 and 2 of 3) is pruned to exactly the fields the outer projection references; the nested-field schema pruning (pruneSerializer / SchemaPruning, gated on serializerNestedSchemaPruningEnabled) is not encoded.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/objects.scala, lines 126-245
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's unconditional core rewrite — `Project(_, SerializeFromObject)` where the outer project uses only a subset of serializer outputs — by modeling serializers as uninterpreted functions `ser0/ser1/ser2` over the input column, with `before()` computing the unused `ser1` and dropping it and `after()` omitting it, so the proof is non-vacuous and reflects the real optimization; the ordinal remapping (outer field 2 → pruned field 1) and symbol reuse of `ser0`/`ser2` are correct, and the SCOPE line honestly marks it PARTIAL because the nested-struct `pruneSerializer`/`SchemaPruning` path (gated on `serializerNestedSchemaPruningEnabled`) is genuinely outside this DSL's bag-semantics model, leaving a specific, non-degenerate, useful rule. ```

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
    "nanos": 334750
  }
}
```
