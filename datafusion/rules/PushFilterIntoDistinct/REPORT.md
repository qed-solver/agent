# PushFilterIntoDistinct

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 9  **Verification rounds used:** 1
**Scope detail:** only the Distinct::All branch is encoded (plain DISTINCT over all columns, modelled as group-by-all-columns with no aggregate calls) and the input has exactly two columns; the Distinct::On branch is not expressible because its "first row among duplicates" selection depends on ordering semantics that QED does not model.


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_filter.rs, lines 870-884
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` (Filter(p, dedup(R))) and `after()` (dedup(Filter(p, R))) are structurally distinct plans sharing the same single scan and the same uninterpreted predicate "p" applied to the same two columns, so the proof establishes the genuine filter/dedup commutation that is the Distinct::All branch of the source rule, not a vacuous identity. The claimed PARTIAL scope is honest and specific: the fixed two-column width is an unavoidable concretization (the DSL offers no uninterpreted schema-width symbol, so any fixed width is equally general and 2 is non-degenerate), and the Distinct::On branch is genuinely inexpressible because its "first row among duplicates" selection requires ordering/arbitrary-choice semantics that QED's bag-semantics model does not have.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7450999
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 26021958
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 878083
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 552125
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19995125
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 26113916
  },
  "total_duration": {
    "secs": 0,
    "nanos": 61737583
  }
}
```
