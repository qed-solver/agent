# InListDedup

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 21  **Verification rounds used:** 2
**Scope detail:** assumes the IN list is a plain disjunction of column-equality disjuncts, so the rule is captured as removing one repeated disjunct from the OR (the DSL has no IN node or literal constants).


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1850-1872
```

## Independent verifier review

**Verdict:** CONFIRMED

The before/after patterns are structurally different (a 4-way OR with one repeated disjunct vs. the deduplicated 3-way OR), so the proof is non-vacuous, and the symbol sharing is exactly right: reusing `p2` models the same list element occurring in both IN lists while `p1`/`p3` stay independent, with no concrete predicates baked in and no missing preconditions (the rule's non-negated, same-column requirements are respected by the encoding, and ∨-idempotency holds even under null/3-valued semantics). Since IN is definitionally a disjunction of column-equalities, expanding the source rule for two lists sharing one element yields precisely `P∨Q∨Q∨S ≡ P∨Q∨S` — so the proven statement is the rule's exact semantic core at minimal arity, over a superset of disjunct kinds (arbitrary uninterpreted predicates, not just equalities). The stated PARTIAL scope is therefore honest and specific (the DSL genuinely lacks IN nodes and value literals, and fixed arity is inherent to concrete patterns), and the special case is non-degenerate.

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
    "nanos": 362500
  }
}
```
