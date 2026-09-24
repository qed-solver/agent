# CommuteRightJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** each join input has exactly one column, and the join condition is a single uninterpreted predicate over the join's two-column row


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

CommuteRightJoin converts a RightJoin to a LeftJoin with the left and right
inputs swapped. This allows other normalization rules to only worry about the
LeftJoin case.

Extracted from `join.opt` (which defines multiple rules — implement specifically `CommuteRightJoin`, not the other rules in that file):

```
# CommuteRightJoin converts a RightJoin to a LeftJoin with the left and right
# inputs swapped. This allows other normalization rules to only worry about the
# LeftJoin case.
[CommuteRightJoin, Normalize, HighPriority]
(RightJoin $left:* $right:* $on:* $private:*)
=>
(LeftJoin $right $left $on (CommuteJoinFlags $private))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous and semantically faithful: before() is a genuine RightJoin(L, R, P) and after() is a LeftJoin(R, L) with the *same* uninterpreted predicate P applied to the correctly permuted column references (L's column at swapped-join ordinal 1, R's at 0), followed by a projection restoring the original (L||R) output column order — exactly the semantic content of CommuteRightJoin, whose only other component (the $private flag commute) is a non-semantic backend attribute, and the source rule has no preconditions that are missing. Sharing one predicate symbol across both sides is correct, not an over-constraint, because the rule reuses the identical ON condition; the single-column-per-input shape restriction is honestly and specifically declared in the PARTIAL tag, and the proved equivalence (right join = swapped left join with consistent null-extension) is the rule's genuine, non-degenerate core claim rather than a distorted or structurally identical one.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 14121252
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 47158751
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1054875
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 791000
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 32102333
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 47377875
  },
  "total_duration": {
    "secs": 0,
    "nanos": 95942958
  }
}
```
