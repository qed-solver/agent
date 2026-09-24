# CalcRemove

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** the trivial Calc's identity projection is over a fixed 2-column input; the original rule applies to any input arity


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/CalcRemoveRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is faithful: before() is a ProjectMany with the identity field list over a scan, and after() is the bare scan — structurally distinct, so the proof is non-vacuous and correctly captures the core claim of CalcRemoveRule (identity projection with no filter is a no-op). The only restriction is fixed 2-column arity, which is an inherent structural limitation of the pattern DSL (no parametric arity exists), not a semantic under-generalization; the column types remain uninterpreted, and the SCOPE line accurately and specifically documents the restriction. No preconditions are missing, no symbols are mis-shared, and the operator shape (ProjectMany of in-order fields, no Filter) exactly matches the "trivial Calc" predicate in the source. ```

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
    "nanos": 356458
  }
}
```
