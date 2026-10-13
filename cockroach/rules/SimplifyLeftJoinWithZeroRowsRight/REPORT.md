# SimplifyLeftJoinWithZeroRowsRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 29  **Verification rounds used:** 2
**Scope detail:** fixed-shape pattern: a 2-column left and a 2-column zero-row right (both uninterpreted scans), versus the source rule's arbitrary-width $left/$right.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

SimplifyLeftJoinWithZeroRowsRight replaces a LeftJoin with a Project when
the right input never returns any rows. The Project passes through columns
from the left input and produces NULL values for each column in the right
input.

Extracted from `join.opt` (which defines multiple rules — implement specifically `SimplifyLeftJoinWithZeroRowsRight`, not the other rules in that file):

```
# SimplifyLeftJoinWithZeroRowsRight replaces a LeftJoin with a Project when
# the right input never returns any rows. The Project passes through columns
# from the left input and produces NULL values for each column in the right
# input.
[SimplifyLeftJoinWithZeroRowsRight, Normalize]
(LeftJoin $left:* $right:* & (HasZeroRows $right))
=>
(Project $left (MakeNullProjections $right) (OutputCols $left))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully mirrors the source rule — a LEFT join whose right input is structurally the empty relation (the exact bag-theoretic reading of `HasZeroRows`) rewritten to a projection that passes the left's columns through and emits NULL literals typed by the right's columns — with independent left/right scans and independent types, and structurally different before/after trees (Join vs. ProjectMany), so the proof is of the actual rewrite, not a vacuous identity. The PARTIAL scope tag is honest and the restriction is genuine rather than an avoidable DSL gap: QED decides equivalence of concrete queries, so universally-quantified arbitrary-width `$left`/`$right` simply cannot be expressed, and the fixed two-column shape remains a non-degenerate special case that still exercises the real optimization (eliminating the join in favor of a null-project for arbitrary left-side content). ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 4097750
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35232917
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 51125
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 351792
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 8256042
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35268417
  },
  "total_duration": {
    "secs": 0,
    "nanos": 46614958
  }
}
```
