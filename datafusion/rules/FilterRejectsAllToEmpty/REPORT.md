# FilterRejectsAllToEmpty

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** assumes the filter predicate is exactly the FALSE literal rather than any predicate that simplifies to RejectsAll (e.g. via AND with a false branch)


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/eliminate_filter.rs, lines 43-236
```

## Independent verifier review

**Verdict:** CONFIRMED

This faithfully encodes the source's RejectsAll→Empty branch restricted to the FALSE-literal sub-case, which the SCOPE line honestly and specifically declares; because a RejectsAll predicate must be a *known* always-false value (an uninterpreted symbol would let QED treat it as possibly-true and the rewrite would be invalid), using the concrete FALSE literal is the correct, non-arbitrary choice rather than an under-generalization. before() (Filter over the scan) and after() (empty Values carrying the identical row type) are structurally distinct, the input schema is preserved exactly as the source requires, and the single shared source symbol is the correct one, so the proof is a genuine non-vacuous bag-semantic equivalence (Filter(false)=empty bag), not a degenerate or over-constrained artifact. ```

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
    "nanos": 334167
  }
}
```
