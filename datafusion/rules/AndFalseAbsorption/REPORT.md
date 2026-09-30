# AndFalseAbsorption

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 6  **Verification rounds used:** 3
**Scope detail:** the absorption is only applied when the false-AND conjunction is the filter condition


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1050-1055
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully mirrors the source rewrite: `before()` is `Filter(AND(false, P))` and `after()` is `Filter(false)` — structurally distinct (no vacuity), with the right operand a fully uninterpreted nullable predicate symbol (matching `right: _` and the "even if A is null" comment), the false literal in the correct left position per `is_false(&left)`, and no silent NOT NULL or other precondition. The only narrowing — instantiating the expression-level rule at the filter condition rather than any boolean position — is genuine and specific, honestly declared in the SCOPE line, and yields a non-degenerate, useful rule (both filters keep exactly zero rows, and QED's proof of bag equality for all instantiations of `P` is exactly the claim that `false AND A` behaves as `false` in selection).

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
    "nanos": 292875
  }
}
```
