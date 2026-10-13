# BinaryOpNullPropagation

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 8  **Verification rounds used:** 1
**Scope detail:** only the Eq branch with a null right operand (eq(value, Null) ⇒ Null) is encoded; DataFusion's rule covers ~30 returns_null_on_null operators in either operand position, and each concrete operator needs its own before()/after() pair since QED cannot derive null propagation for an uninterpreted operator symbol


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 843-856
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-vacuous instance of the simplifier: `Filter(x = NULL)` ⟹ `Filter(NULL::Boolean)` is exactly DataFusion's rewrite (binary expr with a literal-null operand → NULL literal of the expression's type) applied in predicate position for `Eq`, a `returns_null_on_null` operator, and proving it requires the prover to actually know that `x = NULL` is never true — so the proof certifies the null-propagation property itself, not a structural identity. The narrowing to a concrete operator and one operand position is a genuine QED limitation, not a missing DSL capability (uninterpreted operator symbols can be expressed via `RexRN.Pred`, but the prover has no null-propagation axiom for them and must hold for all instantiations, so per-concrete-operator instances are the widest provable form), and the `SCOPE: PARTIAL` line states precisely this restriction. No failure modes triggered: the shared type `V` between the column and the null literal mirrors the source rule's type-coerced null operand rather than an accidental over-constraint, the `returns_null_on_null` guard is instantiated (not silently dropped), and `nullBool` correctly has the comparison's result type, not the operand's.

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
    "nanos": 403125
  }
}
```
