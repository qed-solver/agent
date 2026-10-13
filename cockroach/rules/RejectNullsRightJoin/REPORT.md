# RejectNullsRightJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 10  **Verification rounds used:** 1
**Scope detail:** the null-rejecting filter is restricted to an explicit IS_NOT_NULL on the left input's column (the FullJoin→LeftJoin variant of the rule)


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/reject_nulls.opt

RejectNullsRightJoin is symmetric with RejectNullsLeftJoin. It reduces a
FullJoin operator to a LeftJoin when there is a null-rejecting filter on any
column from the left side.

This rule is marked as high priority for the same reason as
RejectNullsLeftJoin.

Extracted from `reject_nulls.opt` (which defines multiple rules — implement specifically `RejectNullsRightJoin`, not the other rules in that file):

```
# RejectNullsRightJoin is symmetric with RejectNullsLeftJoin. It reduces a
# FullJoin operator to a LeftJoin when there is a null-rejecting filter on any
# column from the left side.
#
# This rule is marked as high priority for the same reason as
# RejectNullsLeftJoin.
[RejectNullsRightJoin, Normalize, HighPriority]
(Select
    $input:(FullJoin $left:* $right:* $on:* $private:*)
    $filters:* &
        (HasNullRejectingFilter $filters (OutputCols $left))
)
=>
(Select (LeftJoin $left $right $on $private) $filters)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a genuine, non-vacuous special case of the source rule: before() uses a FULL join and after() a LEFT join (a real operator difference), with the source rule's semantic precondition "null-rejecting filter on a left column" instantiated as the canonical IS_NOT_NULL on the left column — the one null-rejecting filter QED can actually verify, since an uninterpreted predicate cannot be assumed to reject nulls, making this a fundamental QED limitation rather than a lazy narrowing. Symbol sharing is correct (same uninterpreted ON condition and same filter above the join in both plans, matching the rule's $on/$filters), no preconditions are missing (the identity Filter(FullJoin)≡Filter(LeftJoin) holds for exactly the rows the null-rejecting filter kills: the (NULL,r) null-extended-left rows), and the single-column/single-filter shape loses no semantic generality for this identity; the SCOPE: PARTIAL tag is accurate and specific.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 12957917
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
    "nanos": 75042
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 933542
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 26250667
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 29602292
  }
}
```
