# DeMorganNotAnd

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache DataFusion
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** public record DeMorganNotAnd() implements RRule {


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/utils.rs, lines 319-325
```

## Independent verifier review

**Verdict:** CONFIRMED

before() = Filter(¬(P∧Q)) and after() = Filter((¬P)∨(¬Q)) are structurally distinct and match the source's `not(A and B) ⟹ (not A) or (not B)` exactly, with P and Q as two independent uninterpreted predicates (correct symbol sharing, nothing hard-coded), and the filter-over-scan shape is a neutral vehicle for the scalar identity rather than an added assumption — the identity holds pointwise for all truth assignments (including nulls), so the proof covers the rule's full Boolean content in any relational context, and the nested recursive case is just repeated application of the same universally-proved identity. DataFusion's `negate_clause` And-arm has no preconditions (DeMorgan holds under three-valued logic, and the encoding's nullable predicate types keep nulls in the model), so no precondition is silently dropped, and SCOPE: FULL is honest: the encoding assumes nothing the original rule doesn't actually require. ```

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
    "nanos": 433042
  }
}
```
