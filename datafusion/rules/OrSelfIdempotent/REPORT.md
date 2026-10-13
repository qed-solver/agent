# OrSelfIdempotent

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 12  **Verification rounds used:** 1
**Scope detail:** the containing left expression is assumed to be a single two-predicate OR (left = A OR B, right = A), rather than an arbitrary OR chain that contains A


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 994-999
```

## Independent verifier review

**Verdict:** CONFIRMED

The before/after conditions genuinely differ (`(A OR B) OR A` vs `A OR B` over the same scan), and reusing the single `A` symbol for both occurrences correctly mirrors the source rule's precondition that the right operand be the same expression occurring inside the left OR-chain, so the proof is non-vacuous with correct symbol sharing. `A` and `B` are fully uninterpreted predicates over the shared source row — no concrete predicate, join kind, or precondition was baked in — and since the proven absorption identity `(A∨B)∨A ≡ A∨B` holds for all instantiations of `A`, `B`, it also covers arbitrary longer OR chains (any OR-chain containing A is equivalent to `A∨B` for some `B`), making the `SCOPE: PARTIAL` tag conservative rather than restrictive: the encoding is semantically a full, faithful generalization of the rule.

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
    "nanos": 338125
  }
}
```
