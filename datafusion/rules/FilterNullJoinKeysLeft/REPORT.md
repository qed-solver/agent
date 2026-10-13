# FilterNullJoinKeysLeft

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 8  **Verification rounds used:** 1
**Scope detail:** only the Inner Join case is modeled with two equi-join keys where both LEFT keys are nullable; the rewrite adds the IS NOT NULL filter to the LEFT input only (the right input is left unchanged)


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/filter_null_join_keys.rs, lines 1-109
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-trivial special case: before() and after() are structurally different (an extra Filter is added on the left input), the INNER join with regular EQUALS correctly models the source rule's non-null-aware, NullEqualsNothing precondition, and the proof genuinely requires NULL semantics (IS NOT NULL on nullable keys is redundant only because NULL = anything is never TRUE in an inner equi-join). The SCOPE line honestly and specifically states the restrictions (INNER join only, two keys, both left keys nullable, left-side filter only), each of which is a genuine narrowing of the full source rule that handles multiple join types, any key count, per-key nullability checks, and both sides. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8024168
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35205250
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 832667
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 427583
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21366583
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35337167
  },
  "total_duration": {
    "secs": 0,
    "nanos": 72470750
  }
}
```
