# JoinConditionExpandIsNotDistinctFrom

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 65  **Verification rounds used:** 5
**Scope detail:** IS NOT DISTINCT FROM is the entire condition of an inner join between two single-column scans of a single shared type, with the 3-valued-logic expansion on the right-hand side rather than Calcite's COALESCE-based form


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/JoinConditionExpandIsNotDistinctFromRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-degenerate special case: before (INNER join on IS_NOT_DISTINCT_FROM) and after (INNER join on the 3-valued-safe expansion (x IS NULL AND y IS NULL) OR IS_TRUE(x=y)) are genuinely different and QED-proven equivalent, with the expansion being semantically identical to IS NOT DISTINCT FROM (a valid alternative to Calcite's COALESCE form, as disclosed). The narrowing to an inner join / whole-condition / single shared column is real, specific, and honestly tagged PARTIAL, with no triviality, symbol-sharing, or missing-precondition defects that would make the proof misleading. ```

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
    "nanos": 347291
  }
}
```
