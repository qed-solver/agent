# AndOrAbsorption

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache DataFusion
**Porter attempts used:** 8  **Verification rounds used:** 1
**Scope detail:** public record AndOrAbsorption() implements RRule {


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1096-1102
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous (`Filter(A AND (A OR B))` vs `Filter(A)`) and uses genuinely uninterpreted predicates `A`/`B`, with `A` correctly shared as the identical symbol in both the AND-left and the OR and `B` kept independent — exactly the absorption premise — and the correct relational/boolean shape (a Filter whose condition is `A ∧ (A ∨ B)`). QED's success therefore reflects a real universal tautology `∀A B: A ∧ (A ∨ B) ≡ A` checked pointwise over rows, not a structural coincidence or a hidden precondition (no PK/NOT NULL is needed). It captures the canonical orientation; the commutative variants (`A∧(B∨A)`, `(A∨B)∧A`) are the same boolean identity, so `SCOPE: FULL` is fair and the result is non-degenerate.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5871584
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 132135250
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 862292
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 334333
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16537209
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 132413625
  },
  "total_duration": {
    "secs": 0,
    "nanos": 164210709
  }
}
```
