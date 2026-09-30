# TryDecorrelateInnerLeftJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 27  **Verification rounds used:** 4
**Scope detail:** plain (non-Apply) InnerJoin/LeftJoin over single-column scans; on and innerOn are uninterpreted 2-ary predicates


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateInnerLeftJoin tries to decorrelate a LeftJoin operator nested
beneath an InnerJoin operator by using the associative identity to pull up the
left join to become the outer join. This may be enough to decorrelate the
outer join, or it may allow any outer column references to continue to journey
upwards.

Citations: [1] (see identity #6)

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateInnerLeftJoin`, not the other rules in that file):

```
# TryDecorrelateInnerLeftJoin tries to decorrelate a LeftJoin operator nested
# beneath an InnerJoin operator by using the associative identity to pull up the
# left join to become the outer join. This may be enough to decorrelate the
# outer join, or it may allow any outer column references to continue to journey
# upwards.
#
# Citations: [1] (see identity #6)
[TryDecorrelateInnerLeftJoin, Normalize]
(InnerJoin | InnerJoinApply
    $left:*
    $right:* &
        (HasOuterCols $right) &
        (LeftJoin
            $innerLeft:*
            $innerRight:*
            $innerOn:*
            $innerPrivate:*
        )
    $on:* & (FiltersBoundBy $on (OutputCols2 $left $innerLeft))
    $private:*
)
=>
(LeftJoinApply
    ((OpName) $left $innerLeft $on $innerPrivate)
    $innerRight
    $innerOn
    $private
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding correctly captures the source rule's structural identity — `L INNER JOIN (IL LEFT JOIN IR ON innerOn) ON on(L,IL)` ⟹ `(L INNER JOIN IL ON on(L,IL)) LEFT JOIN IR ON innerOn(IL,IR)` — with the critical `FiltersBoundBy` precondition enforced by applying `on` only to L and IL fields (never IR), and the `before`/`after` trees are genuinely different join groupings (non-trivial proof). The SCOPE: PARTIAL line honestly and specifically notes the narrowing to plain non-Apply joins over single-column scans, which is a genuine QED limitation (correlated/Apply joins have no bag-semantic model) while the algebraic reassociation identity itself is fully and faithfully represented.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 17407417
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 44736876
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 927916
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1069125
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 39808625
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 44995708
  },
  "total_duration": {
    "secs": 0,
    "nanos": 101681833
  }
}
```
