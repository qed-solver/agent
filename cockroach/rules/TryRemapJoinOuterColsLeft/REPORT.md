# TryRemapJoinOuterColsLeft

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 83  **Verification rounds used:** 6
**Scope detail:** the InnerJoinApply case only: both inputs are single-column scans of one shared uninterpreted type, ON is the genuine equality l.c0 = r.c0, and the remapped predicate is an uninterpreted 1-ary h over the outer column, swapped to the equal right column


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryRemapJoinOuterColsLeft is similar to TryRemapJoinOuterColsRight, but it
applies to the left input of a join.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryRemapJoinOuterColsLeft`, not the other rules in that file):

```
# TryRemapJoinOuterColsLeft is similar to TryRemapJoinOuterColsRight, but it
# applies to the left input of a join.
[TryRemapJoinOuterColsLeft, Normalize]
(InnerJoin | InnerJoinApply | SemiJoin | SemiJoinApply
    $left:* & (HasOuterCols $left)
    $right:*
    $on:* &
        (CanMaybeRemapOuterCols $left $on) &
        (Let ($remapped $ok):(TryRemapOuterCols $left $on) $ok)
    $private:*
)
=>
((OpName) $remapped $right $on $private)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a non-vacuous, honest PARTIAL special case: it models the InnerJoinApply variant where a correlated INNER join enforces L.col0 = R.col0, and proves that filtering the join output by uninterpreted h(L.col0) is equivalent to filtering by h(R.col0) — exactly the algebraic core of TryRemapJoinOuterColsLeft's outer-column-to-equal-column remap. The SCOPE line accurately discloses the narrowings (single-column scans of one shared type, equality ON, one-arity uniterated predicate, filter-above-join shape rather than remap-inside-left), and the proof is genuinely non-trivial since the two sides reference different fields whose equality is only guaranteed by the correlate condition. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7507167
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34970083
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 840500
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 494500
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19453750
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35075584
  },
  "total_duration": {
    "secs": 0,
    "nanos": 70305750
  }
}
```
