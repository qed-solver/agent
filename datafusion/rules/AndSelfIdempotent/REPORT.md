# AndSelfIdempotent

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 7  **Verification rounds used:** 2
**Scope detail:** the duplicated right operand is a single uninterpreted predicate occurring as a nested conjunct inside the left operand, not an arbitrary subexpression of any shape or depth.


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1084-1089
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-vacuous instance of the DataFusion rule: the shared symbol B is exactly the duplicated conjunct the original's `expr_contains(&left, &right, And)` side condition requires (it appears both nested inside `left` via AND-only paths and as the right operand), A and C are genuinely independent uninterpreted predicates (no wrong sharing), and before/after are structurally different, so the zero-SMT-time result just reflects that normalization proves the real idempotency equivalence `((A∧B)∧C)∧B ≡ (A∧B)∧C`, which holds even under null semantics with no missing preconditions. The narrowing — left fixed to a specific 3-conjunct AND shape and right restricted to a single uninterpreted predicate — is genuine, since the original's parametric "right is an arbitrary subexpression of any shape AND-embedded at any depth in left" cannot be expressed in the DSL (substructure-containment patterns go beyond QED's flat uninterpreted-symbol semantics), and the PARTIAL scope line states this honestly and specifically. The result is still a useful, non-degenerate rule (a filter whose condition redundantly re-ANDs a conjunct already nested inside it can drop the duplicate), so the provable verdict is meaningful. ```

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
    "nanos": 491209
  }
}
```
