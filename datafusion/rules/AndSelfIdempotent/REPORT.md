# AndSelfIdempotent

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 3  **Verification rounds used:** 1
**Scope detail:** the duplicated right operand is a single uninterpreted predicate occurring as a nested conjunct inside the left operand, not an arbitrary subexpression of any shape or depth.


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1084-1089
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding correctly captures the core idempotency property (left AND right ≡ left when right is a conjunct of left) with B shared between the left tree and the outer AND, making before() and after() structurally distinct yet semantically equivalent; the narrowing to a single atomic predicate as the duplicated subexpression is a genuine DSL limitation (no subexpression meta-variable exists) and is honestly declared in the SCOPE line, while all other aspects (AND operator, filter shape, symbol reuse) faithfully mirror the DataFusion rule.

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
    "nanos": 123333
  }
}
```
