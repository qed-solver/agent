# IsUnknownToIsNull

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 29  **Verification rounds used:** 3
**Scope detail:** only the IsUnknown branch is encoded; IsNull(expr) → false on non-nullable expr is not covered


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1808-1811
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is faithful and non-trivial: it correctly models the side condition by declaring the scan column non-nullable (`varType("X_Type", false)`), uses `SqlStdOperatorTable.IS_UNKNOWN` (matching DataFusion's `IsUnknown` semantics — true iff the operand is NULL/unknown), and proves `σ_{IS_UNKNOWN(x)}(R) = σ_{false}(R)` by relying on QED's nullability constraint. The partial scope (omitting the `IsNull` branch) is explicitly and honestly disclosed in the SCOPE comment, and the proved equivalence is genuinely non-vacuous — it requires the non-nullability guarantee, not just structural identity.

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
    "nanos": 284041
  }
}
```
