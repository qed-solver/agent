# NotIsNullToIsNotNull

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache DataFusion
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** public record NotIsNullToIsNotNull() implements RRule {


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/utils.rs, lines 341-344
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding matches the named rule exactly — Filter(R, NOT(R.f IS NULL)) vs. Filter(R, R.f IS NOT NULL) over a fully uninterpreted scan with a nullable VarType — so before() and after() are structurally distinct, and the proven identity is the genuine SQL null-logic duality (NOT∘IS_NULL ≡ IS_NOT_NULL, neither side ever NULL), not a vacuity or coincidence of shared symbols. The sibling arm in the source block (NOT(A IS NOT NULL) ⟹ A IS NULL) is a separate symmetric rule that would be its own RRule, not an assumption inside this one, so the FULL tag is honest for the rule as named; nullability is modeled rather than away, which is what makes the claim non-trivial.

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
    "nanos": 636834
  }
}
```
