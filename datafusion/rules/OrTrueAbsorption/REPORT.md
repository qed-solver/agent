# OrTrueAbsorption

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache DataFusion
**Porter attempts used:** 3  **Verification rounds used:** 1
**Scope detail:** public record OrTrueAbsorption() implements RRule {


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 966-971
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the source rule: an uninterpreted predicate `left` stands in for the arbitrary left expression `_`, `RexRN.trueLiteral()` on the right matches the `is_true(&right)` guard (and correctly fires only for the right-operand-true case the rule specifies), and `before()`/`after()` are structurally distinct (filter on `Or(A,true)` vs filter on `true`), so the proof is non-vacuous. The null-safety comment ("even if A is null") is precisely the justification that no extra precondition (e.g. NOT NULL) is needed — in 3VL, `A OR true = true` unconditionally — so the absence of constraints is faithful, and the filter-context embedding with a fully general uninterpreted A is the most general relational form of this scalar rewrite, warranting `SCOPE: FULL`.

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
    "nanos": 61083
  }
}
```
