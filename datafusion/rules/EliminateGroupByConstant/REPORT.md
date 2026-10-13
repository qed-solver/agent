# EliminateGroupByConstant

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 11  **Verification rounds used:** 1
**Scope detail:** one bare-column group key, one redundant group expression that is a single uninterpreted (deterministic) function of that key, one non-distinct aggregate over a second input column; the general rule (arbitrary deterministic expressions over constants and any subset of the bare group columns) reduces to the same shape since QED's uninterpreted scalar symbols stand in for those expressions.


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/eliminate_group_by_constant.rs, lines 1-151
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` (group set [k, h(k)]) and `after()` (group set [k] + top projection re-deriving h(k)) are structurally distinct and exactly mirror DataFusion's rewrite — simplified aggregate plus schema-preserving projection — with `h` and `f` correctly shared as uninterpreted symbols, so the source rule's "deterministic function of the bare group columns" precondition holds by construction (h is a function of the retained key k, and the non-empty `required`-keys bail-out is satisfied since one bare key remains). The PARTIAL scope line is honest and specific (one bare key, one redundant expression, one non-distinct aggregate over a second column), and within that shape the proof is fully general over the expression's form (any deterministic expression, including pure constants, is stood in for by uninterpreted h) and the aggregate, with no hidden preconditions, wrong operators, or vacuous identity.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 10252874
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 47182876
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 936875
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 803042
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 25227000
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 47397375
  },
  "total_duration": {
    "secs": 0,
    "nanos": 88593708
  }
}
```
