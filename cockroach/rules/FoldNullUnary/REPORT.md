# FoldNullUnary

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 48  **Verification rounds used:** 3
**Scope detail:** only the unary-minus branch (-(Null) ⇒ Null) is encoded; other unary operators are structurally identical but each requires its own before()/after() pair


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldNullUnary discards any unary operator with a null input, and replaces it
with a null value having the same type as the unary expression would have.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldNullUnary`, not the other rules in that file):

```
# FoldNullUnary discards any unary operator with a null input, and replaces it
# with a null value having the same type as the unary expression would have.
[FoldNullUnary, Normalize]
(Unary $input:(Null))
=>
(FoldNullUnary (OpName) $input)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's structure for the unary-minus instance: `Project(UNARY_MINUS(NULL))` vs `Project(NULL)` over the same scan, with a null *literal* input (correctly matching `$input:(Null)`, not a column reference), and no dropped preconditions since the source's unary rule (unlike the binary ones) carries no guard. The narrowing to a concrete operator is a genuine QED limitation, not a fixable DSL gap — an uninterpreted unary symbol would require proving `op(NULL) = NULL` for an uninterpreted function, which QED cannot derive — and the PARTIAL scope line states exactly this restriction, so the fast SMT result proves a real, sound, non-degenerate special case (-NULL = NULL), with before()/after() structurally different.

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
    "nanos": 108959
  }
}
```
