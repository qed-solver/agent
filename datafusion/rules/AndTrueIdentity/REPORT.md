# AndTrueIdentity

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache DataFusion
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** public record AndTrueIdentity() implements RRule {


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1044-1049
```

## Independent verifier review

**Verdict:** CONFIRMED

before() = Filter(AND(true, P), S) is structurally different from after() = Filter(P, S), so the proof is non-vacuous, and the encoding mirrors the source exactly: the literal-true left operand matches both the rule's direction ("true AND A") and its is_true(&left) guard, while the right operand is a fully uninterpreted predicate over a scan with an uninterpreted type, so no concrete predicate or relation is baked in. The filter-over-scan shape is the standard relational embodiment of a pure predicate identity in this DSL (the same convention as the reference FilterMerge example), since true∧A ≡ A is the same pointwise propositional fact in any Boolean context and holds exactly under three-valued/bag semantics, so no precondition (nullability, uniqueness, etc.) is silently missing and the SCOPE: FULL tag is honest. ```

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
    "nanos": 301459
  }
}
```
