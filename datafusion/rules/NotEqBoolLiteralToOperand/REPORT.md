# NotEqBoolLiteralToOperand

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 8  **Verification rounds used:** 1
**Scope detail:** encodes only the `true != A --> !A` arm of the source rule (A an arbitrary boolean expression); the `false != A --> A` and `null != A --> null` arms have distinct rewrite targets that one before/after pair cannot express


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 921-934
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the `true != A → !A` arm: `before()` uses `NOT_EQUALS(true_literal, A)` and `after()` uses `Not(A)`, which are structurally distinct yet semantically equivalent (including the NULL case), so the SMT proof is non-vacuous. The single uninterpreted predicate `A` over a minimal single-column scan is the right level of generality, and the `SCOPE: PARTIAL` tag honestly and specifically acknowledges that the `false != A → A` and `null != A → null` arms have different rewrite targets that a single before/after pair cannot express — a genuine DSL limitation, not an encoding shortcut. ```

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
    "nanos": 320958
  }
}
```
