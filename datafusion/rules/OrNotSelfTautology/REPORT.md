# OrNotSelfTautology

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 64  **Verification rounds used:** 4
**Scope detail:** A is modeled as a non-nullable boolean column of the scanned source, not an arbitrary uninterpreted non-null boolean expression


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 978-985
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-trivial (before carries a real `a OR NOT a` filter that after removes) and correctly captures the DataFusion rule's essential semantics and its "A is not null" precondition, since the column is declared non-nullable — with a nullable A, `A OR NOT A` is null in 3VL and the filter would drop rows, so the proof would genuinely fail without this declaration. The PARTIAL tag is honest and specific: A is restricted to a bare non-null boolean column of a scan rather than an arbitrary non-null expression, which is forced by the toolchain because the DSL's uninterpreted predicates are nullable (`genericPredicateOp(name, true)`) and per-expression nullability is never serialized to the prover (only table-column nullability is), making the column the only way to express the precondition — a genuine limitation, not an avoidable shortcut, and the rule remains non-degenerate.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6041000
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35630500
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 934209
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 468583
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16522709
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35707750
  },
  "total_duration": {
    "secs": 0,
    "nanos": 67970792
  }
}
```
