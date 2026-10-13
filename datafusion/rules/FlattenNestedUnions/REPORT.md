# FlattenNestedUnions

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** fixed-shape instance of the flatten: a 3-input UNION ALL with a nested 2-branch UNION ALL in the second input position, flattened to a 4-branch union (DataFusion's rule handles arbitrary nesting depth/arity, and also removes single-input unions and pushes projections down).


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/optimize_unions.rs, lines 63-74
```

## Independent verifier review

**Verdict:** CONFIRMED

before() (Union(A, Union(B, C), D)) and after() (Union(A, B, C, D)) are structurally distinct, so the proof is the genuine, non-vacuous claim that flattening a nested union-all preserves semantics — exactly the soundness property of DataFusion's `extract_plans_from_union` transform. `union(true, ...)` correctly models DataFusion's n-ary bag (UNION ALL) union (DataFusion has no set-union variant), all branches share the single uninterpreted type T as a union's schema requires, and A/B/C/D are independent scan symbols with no sharing, so the proof isn't an artifact of over-constraint. The fixed 3-input / nested-2-in-second-position shape is a real limitation of the pattern DSL (no inductive pattern variables exist to express arbitrary nesting depth/arity), it is honestly and specifically tagged `SCOPE: PARTIAL`, and the instance is non-degenerate, making this a faithful special case of the rule.

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
    "nanos": 831958
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 644375
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
    "nanos": 1790834
  }
}
```
