# EqSelfToNotNull

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** only the nullable branch of the source rule is encoded (A = A → A IS NOT NULL OR NULL for nullable A); the non-nullable branch (A = A → true) is a distinct rewrite target a single before/after pair cannot cover


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 899-917
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding mirrors the source rule's exact structure — the same uninterpreted column `x` appears on both sides of `EQUALS` (faithfully capturing the `left == right` precondition, so the proof is not a coincidental match of independent symbols), the target is precisely `IS_NOT_NULL(x) OR NULL-literal`, and the source column is correctly marked nullable to match the selected branch; the proof is non-vacuous because the equivalence is the genuinely non-trivial 3-valued identity (both sides NULL when x is NULL, both TRUE otherwise), and QED's ability to prove it confirms the standard operators were interpreted semantically rather than as unrelated uninterpreted predicates. The SCOPE tag is honest and specific: the non-nullable branch (`A = A → true`) has a different RHS under a different nullability assumption and genuinely cannot share a single before/after pair, so restricting to the nullable branch is a real limitation of the encoding, not a vacuity — and the remaining rule is a meaningful, non-degenerate optimization.

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
    "nanos": 98333
  }
}
```
