# BinaryOpNullPropagation

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 30  **Verification rounds used:** 2
**Scope detail:** only the Eq branch with a null right operand (eq(value, Null) ⇒ Null) is encoded; DataFusion's rule covers ~30 returns_null_on_null operators in either operand position, and each concrete operator needs its own before()/after() pair since QED cannot derive null propagation for an uninterpreted operator symbol


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 843-856
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is faithful to the special case it claims: `before()` (filter on `EQUALS(x, NULL-lit)`) and `after()` (filter on a bare NULL-Bool literal) are structurally distinct, and the proof is non-vacuous — it depends on real three-valued null semantics (x = NULL is never true, so both filters keep no rows), not a structural coincidence. The narrowing from DataFusion's ~30 `returns_null_on_null` operators in either operand position down to just Eq with a null right operand is genuine rather than a DSL gap: null propagation is the internal semantics of each concrete interpreted operator, which QED's SMT model knows only for built-ins (like Calcite's EQUALS) and cannot assume of an uninterpreted operator symbol, so the fully general form is unprovable; this restriction is disclosed accurately and specifically in the SCOPE line. Preconditions are honored (the null operand is a literal, matching DataFusion's `is_null` guard, and the folded literal carries the expression's Boolean type), with no symbol-sharing or hard-coded-shape errors. ```

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
    "nanos": 88917
  }
}
```
