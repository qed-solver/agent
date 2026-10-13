# JoinPushExpressions

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 37  **Verification rounds used:** 2
**Scope detail:** single-column join inputs, one pushed expression per side, one residual predicate, inner join


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/JoinPushExpressionsRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

before() and after() are genuinely structurally different and differ in exactly the way the real rule rewrites — a bare inner join versus a join over child projects that carry FL/FR, with the condition rewritten to reference the projected expression columns and a final projection restoring the original (L.c0, R.c0) row type — and FL, FR, EQ, and RES are shared across both sides while remaining mutually independent, so the SMT equivalence is non-vacuous and not a product of over-constrained symbol reuse. The scope line is honest and specific: the encoding is a real, non-degenerate special case (inner join, one equal conjunct, one residual predicate), and "single-column join inputs" is an artifact of the DSL's one-column scan representation rather than a semantic weakening, since FL/FR/RES are already uninterpreted functions/predicates over the whole row, which is all the rule's correctness depends on. No preconditions (keys, nullness) are required by the source rule's transformation and none are silently omitted, so the proved claim is a faithful instance of JoinPushExpressions.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7634458
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36024292
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 856667
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 542209
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19657208
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36137375
  },
  "total_duration": {
    "secs": 0,
    "nanos": 71744041
  }
}
```
