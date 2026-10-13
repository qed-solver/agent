# SemiJoinJoinTranspose

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 66  **Verification rounds used:** 3
**Scope detail:** single-column X/Y/Z, INNER lower join, and all semi-join keys referencing only X (the source rule's left-push branch; keys from both X and Y are rejected by the rule itself)


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinJoinTransposeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is faithful: before() = (X ⋈_C Y) ⋉_S Z and after() = (X ⋉_S Z) ⋈_C Y are structurally different plans whose bag-equality is exactly the source rule's left-push transposition (no vacuity), and the flat-column arithmetic is correct — the semi-join emits only the left's columns, Z sits at flat 2 in before and flat 1 in after, and reapplying the same sOp/cOp symbols onto the re-anchored columns correctly models the rule's RexInputConverter adjustment while preserving the lower join's condition (join.copy) via shared C. All of the source rule's gating preconditions are either encoded or subsumed: upper join is SEMI, lower join is not semi and satisfies canPushLeftFromAbove (INNER does), and S referencing only X (plus Z) captures the nKeysFromX > 0 branch, in which the rule rejects mixed X/Y keys anyway. The remaining restrictions — single-column scans, INNER lower join rather than the full set of pushable join types, and left-push branch only — are genuine, non-degenerate, and accurately declared in the SCOPE: PARTIAL line, with no concrete predicates or join types baked in that the rule doesn't require. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9741748
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36482084
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 810541
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 660958
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 24380958
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36606584
  },
  "total_duration": {
    "secs": 0,
    "nanos": 76951167
  }
}
```
