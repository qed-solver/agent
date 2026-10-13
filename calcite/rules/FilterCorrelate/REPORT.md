# FilterCorrelate

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 128  **Verification rounds used:** 5
**Scope detail:** INNER correlate only, modeled as an inner join on a true condition; non-correlated predicates are split into left-only (pushed to left), right-only (pushed to right), and both-sides (kept above)


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterCorrelateRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

before() and after() are structurally different (side-local conjuncts pushed below the join while the cross-side conjunct stays above), the three predicate symbols and two scans are shared correctly and independently across the two patterns, and the successful proof itself rules out index miswiring — e.g. p_right pointing at the left column would not have proved equivalent. The shape matches FilterCorrelateRule's actual behavior for its INNER, non-correlated case (left-only → left, right-only → right, rest kept above), with INNER eliminating the null-generation preconditions the general rule handles via join-type flags, and since the DSL exposes no correlate operator the true-condition inner-join model preserves every semantic aspect the transformation depends on (predicate side-locality, bag semantics, no row loss), so the one-line PARTIAL scope tag is specific and honest rather than a degenerate or misleading narrowing.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6985332
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34817292
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 865416
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 523542
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19091792
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34922667
  },
  "total_duration": {
    "secs": 0,
    "nanos": 69803125
  }
}
```
