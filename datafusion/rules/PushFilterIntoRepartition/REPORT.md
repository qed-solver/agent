# PushFilterIntoRepartition

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache DataFusion
**Porter attempts used:** 6  **Verification rounds used:** 1
**Scope detail:** the repartition is modeled by its exact bag semantics: a schema-preserving pass-through that redistributes (permutesh) the input's rows without changing them or their multiplicities, which in QED is an identity projection; the filter predicate is a single uninterpreted predicate over the whole row.


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_filter.rs, lines 865-869
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is faithful: Repartition's bag semantics is exactly a schema-preserving pass-through, so the identity projection is the correct (and only) bag-level model, the single uninterpreted predicate P is shared across both sides (matching the rule relocating the same filter), and the source rule is unconditional — no partitioning-strategy or schema preconditions are missing. before() = Filter(P, Repartition(Source)) vs after() = Repartition(Filter(P, Source)) is a genuine structural commutation (filter above vs below the pass-through), not a vacuous identity, over an arbitrary unconstrained multi-column source; fixing the arity at three columns with P over all of them is not restrictive since P is fully uninterpreted, so the SCOPE: FULL claim is accurate.

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
    "nanos": 357625
  }
}
```
