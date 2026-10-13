# OrAndAbsorption

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache DataFusion
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** public record OrAndAbsorption() implements RRule {


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1006-1011
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` and `after()` are structurally different filters over the same scan whose conditions are `A OR (A AND B)` vs `A`, with A and B as uninterpreted predicates — exactly DataFusion's pattern, and the source guard `is_op_with(And, &right, &left)` (the And must contain the Or's left child as one of its operands) is captured precisely by reusing the symbol A in both positions, while B stays independent. The absorption law is a Boolean tautology that holds pointwise even under 3-valued/null semantics, so no preconditions are missing, and there is no over-constraining: QED proving it confirms the genuine identity, not an artifact of accidental sharing. Embedding the scalar simplification in a filter over a scan is the DSL's standard full expression of this rule (the `B AND A` mirror form is the same semantic claim since AND is commutative), so `SCOPE: FULL` is honest and the proof is non-vacuous. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5549083
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34549041
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 805208
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 299792
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16161542
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34637458
  },
  "total_duration": {
    "secs": 0,
    "nanos": 65911250
  }
}
```
