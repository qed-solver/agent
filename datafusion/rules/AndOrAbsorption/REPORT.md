# AndOrAbsorption

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache DataFusion
**Porter attempts used:** 12  **Verification rounds used:** 1
**Scope detail:** public record AndOrAbsorption() implements RRule {


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1096-1102
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` is structurally distinct from `after()` — `filter(A AND (A OR B))` vs `filter(A)` — with the single uninterpreted predicate `A` shared at exactly the two positions the source rule requires (left conjunct and disjunct, matching `is_op_with(Or, &right, &left)`) and `B` as a separate symbol, so the proof is of the real absorption law, not a vacuous or over-constrained one. `A` and `B` remain fully uninterpreted over a generic scan, and the identity `A ∧ (A ∨ B) ≡ A` holds under SQL three-valued/null semantics as well, so no NOT NULL or other precondition is silently missing and nothing the rule leaves open is hard-coded. For the rule arm shown, the FULL scope tag is honest: the single-column scan only fixes the row-level context for a purely propositional law, not `A`/`B` themselves, so no narrower special case was assumed.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5246709
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 26350708
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 811166
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 294500
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16035834
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 26449417
  },
  "total_duration": {
    "secs": 0,
    "nanos": 57655458
  }
}
```
