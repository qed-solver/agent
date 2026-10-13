# RightJoinNullRejectingToInner

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** the null-rejecting filter is restricted to an explicit IS_NOT_NULL on the left input's column, and only the RightJoin→InnerJoin variant of eliminate_outer is modeled (other join-type branches and projection inlining are not)


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/eliminate_outer_join.rs, lines 102-244
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous and semantically correct: `before()` and `after()` differ exactly in join kind (RIGHT vs INNER), the filter `IS_NOT_NULL` on field 0 is the left side of the join row (the null-extended side of a right join), and QED must genuinely reason about null-extended rows to prove the equivalence — so this is the real soundness lemma of the `eliminate_outer` branch `(Right, true, _) => Inner`. The predicate choice is forced rather than lazy: with an uninterpreted predicate the equivalence would be false in general (a predicate could admit padded rows) and QED cannot state the "null-rejecting on left" side condition, so instantiating the canonical `IS_NOT_NULL` is the correct way to capture the rule's core; the `on` condition and filter symbols are properly shared across both sides, `L`/`R` scans are independent, and no spurious preconditions (PK/NOT NULL) are assumed. The SCOPE: PARTIAL line is accurate and specific — only the Right→Inner branch, an explicit IS_NOT_NULL filter, and no projection inlining are modeled, which is a genuine, non-degenerate special case honestly declared as such.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6809334
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
    "nanos": 881125
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 649292
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19440708
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 34932750
  }
}
```
