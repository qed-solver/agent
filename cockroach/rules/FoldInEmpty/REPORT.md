# FoldInEmpty

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 8  **Verification rounds used:** 1
**Scope detail:** the scalar In predicate over an empty tuple is modeled as a relational semi-join against a zero-row relation, since QED has no scalar In predicate or list constant.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldInEmpty replaces the In with False when the the right input is empty. Note
that this is correct even if the left side is Null, since even an unknown
value can't be in an empty set.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldInEmpty`, not the other rules in that file):

```
# FoldInEmpty replaces the In with False when the the right input is empty. Note
# that this is correct even if the left side is Null, since even an unknown
# value can't be in an empty set.
[FoldInEmpty, Normalize]
(In * (Tuple []))
=>
(False)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous: a SEMI join against a zero-row relation is empty, which is the relational analogue of IN over an empty tuple being false. The left relation is uninterpreted and the SEMI join kind matches IN specifically, so it is not a vacuous structural identity or an accidental over-constraint. The PARTIAL scope honestly states that scalar IN and empty tuple constants are approximated by a relational semi-join, making this a genuine, non-degenerate special case rather than the full expression-level rule.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 114916
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
    "nanos": 1076458
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 261500
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 9241500
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 25550417
  }
}
```
