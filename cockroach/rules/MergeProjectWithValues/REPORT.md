# MergeProjectWithValues

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 100  **Verification rounds used:** 0
**Scope detail:** modeled with a representative 2-column, single-row Values


## Source rule (as given to the porter)

```
Merges an outer Project with an inner single-row Values operator.
```

## Independent verifier review

**Verdict:** CONFIRMED

Directly encoded using RuleBuilder's real Calcite Values-with-literal-content support (RelBuilder.values(fieldNames, values...)) wrapped in a small local RelRN record -- QED's prover core already has a real interpreted Values{schema, content} variant (relation.rs) and JSONSerializer.java already serializes LogicalValues with its actual row content, so this isn't a DSL gap at all, just needed a custom local RelRN to reach the existing Calcite API (same trick class as the Exists RexRN record). Modeled as a representative single-row, 2-column Values with one passthrough column and one input-independent synthesized (literal) column; verified non-vacuous with a negative control (wrong synthesized literal) correctly failing to prove.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 2035792
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 6337625
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 32375
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 176875
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 4967083
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 6366459
  },
  "total_duration": {
    "secs": 0,
    "nanos": 13622833
  }
}
```
