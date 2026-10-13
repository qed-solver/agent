# SimplifyRightJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 6  **Verification rounds used:** 1
**Scope detail:** self-join in which both inputs are the same single non-nullable-column scan joined on equality of that column


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

SimplifyRightJoin reduces a FullJoin operator to a LeftJoin operator when it's
known that every row in the join's right input will match at least one row in
the left input. This rule is symmetric with SimplifyLeftJoin; see that rule
for more details and examples.

Extracted from `join.opt` (which defines multiple rules — implement specifically `SimplifyRightJoin`, not the other rules in that file):

```
# SimplifyRightJoin reduces a FullJoin operator to a LeftJoin operator when it's
# known that every row in the join's right input will match at least one row in
# the left input. This rule is symmetric with SimplifyLeftJoin; see that rule
# for more details and examples.
[SimplifyRightJoin, Normalize]
(FullJoin
    $left:*
    $right:*
    $on:* & (JoinFiltersMatchAllLeftRows $right $left $on)
    $private:*
)
=>
(LeftJoin $left $right $on $private)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding uses the correct operators (FULL→LEFT on the same shared `cond`), and `before()`/`after()` genuinely differ in join kind so the proof is non-vacuous; in the chosen shape (self-join of one non-nullable column on `x = y`) the source rule's precondition "every right row matches a left row" holds by construction — each right row matches itself, so no unmatched-right NULL-extended rows exist — making FULL and LEFT bag-equal (verified: with a NULL present the match fails and FULL≠LEFT, which is exactly why the column is declared non-nullable). The narrowing to a single shared non-null-column self-join with a concrete equality is a heavy but honest `SCOPE: PARTIAL` restriction, and it is forced by a genuine QED limitation (the decision procedure checks unconditional equivalence and cannot express the relational "all right rows match" guard), not by an avoidable DSL gap; it is a non-degenerate, canonical special case (the source's own self-join example) rather than an accidentally over-constrained or vacuous encoding, and there is no missing or mis-stated precondition.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8890543
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
    "nanos": 849416
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1053083
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 22862125
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 38883667
  }
}
```
