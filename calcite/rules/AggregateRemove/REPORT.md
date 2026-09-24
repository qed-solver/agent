# AggregateRemove

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 34  **Verification rounds used:** 2
**Scope detail:** assumes the input is a base table whose single group column is declared unique (key constraint) and the aggregate has no aggregate calls; the splittable-aggregate-function half (e.g. MAX(x) -> x) is not modeled.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateRemoveRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` (a SIMPLE group-by on column 0 with zero agg calls, i.e. SELECT DISTINCT) is structurally and semantically distinct from `after()` (the raw scan), and the rule's essential precondition — `areColumnsUnique(input, groupSet)` — is correctly modeled as the scan's declared key (`unique=true` → `key: [[0]]` in the serialized schema) rather than silently dropped, so the proof is a genuine, non-vacuous identity-over-a-key result. This is a faithful instance of the no-aggregate-calls branch of `AggregateRemoveRule` in the case where input and output field counts match (1 vs 1, so no trailing project is needed, exactly as the source rule would emit); every restriction — single-column base-table input, key on the sole group column, empty agg-call list, and the unmodeled splittable-function half (MAX(x)→x) — is precisely what the `SCOPE: PARTIAL` line states, and the input shape is forced by the DSL, which has no way to attach uniqueness guarantees to derived relations or multi-column scans. Symbol sharing and operator shapes are correct (one shared table symbol, one group column, SIMPLE group type with groupCount > 0, empty function list), so none of the triviality/wrong-operator/missing-precondition failure modes apply and the "provable" verdict is meaningful and honestly scoped.

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
    "nanos": 298167
  }
}
```
