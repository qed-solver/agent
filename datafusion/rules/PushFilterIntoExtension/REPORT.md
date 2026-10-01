# PushFilterIntoExtension

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** assumes a single-input extension with a 2-column input (one pass-through column A and one prevent column derived as F(B, witness)), modeled as an inner join with an auxiliary witness relation, and a predicate split into exactly one pushable conjunct P over A and one kept conjunct Q over (A, F(B, t)).


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_filter.rs, lines 1302-1368
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous and faithful: before() = π·σ_{P(A)∧Q(A,F(B,t))}(S ⋈_J T) and after() = π·σ_{Q(A,F(B,t))}(σ_{P(A)} S ⋈_J T) are structurally different plans whose bag-equivalence is a genuine commutation (the pushed conjunct P references only the pass-through column A, while the kept conjunct Q references the derived prevent column F(B,t) and stays above in both), and it holds for every instantiation of the uninterpreted symbols P, Q, F, J, S, T. The extension's pass-through contract (non-prevent column A preserved unchanged, prevent column arbitrarily derived, rows produced via an arbitrary inner-join relation with a witness) is exactly the precondition under which DataFusion's rule is sound, and the required symbol sharing (same P/Q/F/J objects on both sides via the static fields) is correct rather than coincidentally over-constraining. The narrowing (single input, 2-column schema, exactly one pushed + one kept conjunct) is a genuine, specific, non-degenerate special case and is honestly recorded in the SCOPE line. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5527707
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 7185333
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 79541
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 541084
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 11502375
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 7246208
  },
  "total_duration": {
    "secs": 0,
    "nanos": 21860500
  }
}
```
