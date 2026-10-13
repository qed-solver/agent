# EqBoolLiteralToOperand

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 46  **Verification rounds used:** 3
**Scope detail:** encodes only the `true = A --> A` arm of the source rule (A an arbitrary boolean expression); the `false = A --> !A` and `null = A --> null` arms have distinct rewrite targets that one before/after pair cannot express


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 871-884
```

## Independent verifier review

**Verdict:** CONFIRMED

This is a faithful, non-vacuous encoding of the rule's `true = A → A` arm: `A` is a genuinely uninterpreted boolean predicate over the row (not a hard-coded condition), the `filter(...)` wrapper is the standard faithful relational carrier for a scalar equivalence (so proving the two filter bags equal is equivalent to proving the scalar expressions equal), and `before()` (`true = A`) vs `after()` (`A`) are structurally distinct, so the proof is not vacuous. The restriction to one of the three arms is a real limit of the single before/after rule format (the three arms map to three distinct targets that one pair cannot express, and QED's rule unit is inherently one pair), it is correctly and specifically tagged `PARTIAL`, and no precondition is silently dropped — `A`'s boolean-ness matches the source's `is_boolean_type` guard, and the equivalence `true = A ≡ A` holds in both 2VL and 3VL, so the "provable" result reflects the real semantics.

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
    "nanos": 326791
  }
}
```
