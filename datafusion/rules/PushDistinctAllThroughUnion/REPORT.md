# PushDistinctAllThroughUnion

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** the union has exactly two inputs (DataFusion's rule fires on any number of inputs); both inputs are one-column relations of the same type, as required by the union's schema.


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/optimize_unions.rs, lines 75-96
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully mirrors the rule for its stated scope: DISTINCT is correctly modeled as a group-by-with-no-aggregate-calls (exact for the single-column relations used), DataFusion's `Union` is correctly represented as UNION ALL (`all=true`), and A/B are two independent uninterpreted scans sharing type T, so the proof really quantifies over all such instantiations rather than baked-in values. `before()` (inner `Distinct`s present) is structurally distinct from `after()` (inner `Distinct`s removed) and the equality is genuinely non-vacuous, so the proof captures the actual push-through transformation. The only narrowing (exactly two inputs, one column) is honestly disclosed in the SCOPE line, is specific and non-degenerate, and does not make the result misleading.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 12651252
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 25939709
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1028625
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 681417
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 29896083
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 26169709
  },
  "total_duration": {
    "secs": 0,
    "nanos": 72069667
  }
}
```
