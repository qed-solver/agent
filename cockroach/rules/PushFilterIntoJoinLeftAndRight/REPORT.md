# PushFilterIntoJoinLeftAndRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 83  **Verification rounds used:** 6
**Scope detail:** INNER only, two-column inputs, no outer columns, one mappable cross equality plus two supporting cross equalities


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

PushFilterIntoJoinLeftAndRight pushes a filter into both the left and right
sides of an InnerJoin or SemiJoin if it can be mapped to use the columns of
both sides. For example, consider this query:

SELECT * FROM a JOIN b ON a.x=b.x AND a.y=b.y AND a.x + b.y < 5

In this case, we can map a.x + b.y < 5 to both sides based on the equality
filters a.x=b.x AND a.y=b.y. For the left side, we can map it to
a.x + a.y < 5, and for the right side, we can map it to b.x + b.y < 5.
Given this mapping, we can safely push the filter down to both sides and
remove it from the ON filters list.

Note that this rule is only applied when the left and right inputs do not have
outer columns. If they do, then this rule can cause undetectable cycles with
TryDecorrelateSelect, since the filter is pushed down to both sides, but then
only pulled up from the right side by TryDecorrelateSelect. For this reason,
the rule also does not apply to InnerJoinApply or SemiJoinApply.

NOTE: It is important that this rule is first among the join filter push-down
rules.

Extracted from `join.opt` (which defines multiple rules — implement specifically `PushFilterIntoJoinLeftAndRight`, not the other rules in that file):

```
# PushFilterIntoJoinLeftAndRight pushes a filter into both the left and right
# sides of an InnerJoin or SemiJoin if it can be mapped to use the columns of
# both sides. For example, consider this query:
#
#   SELECT * FROM a JOIN b ON a.x=b.x AND a.y=b.y AND a.x + b.y < 5
#
# In this case, we can map a.x + b.y < 5 to both sides based on the equality
# filters a.x=b.x AND a.y=b.y. For the left side, we can map it to
# a.x + a.y < 5, and for the right side, we can map it to b.x + b.y < 5.
# Given this mapping, we can safely push the filter down to both sides and
# remove it from the ON filters list.
#
# Note that this rule is only applied when the left and right inputs do not have
# outer columns. If they do, then this rule can cause undetectable cycles with
# TryDecorrelateSelect, since the filter is pushed down to both sides, but then
# only pulled up from the right side by TryDecorrelateSelect. For this reason,
# the rule also does not apply to InnerJoinApply or SemiJoinApply.
#
# NOTE: It is important that this rule is first among the join filter push-down
#       rules.
[PushFilterIntoJoinLeftAndRight, Normalize]
(InnerJoin | SemiJoin
    $left:* & ^(HasOuterCols $left)
    $right:* & ^(HasOuterCols $right)
    $on:[
        ...
        $item:* &
            ^(FiltersItem (Eq (Variable) (Variable))) &
            (CanMapJoinOpFilter
                $item
                $leftCols:(OutputCols $left)
                $equivSet:(GetEquivGroups $on $left $right)
            ) &
            (CanMapJoinOpFilter
                $item
                $rightCols:(OutputCols $right)
                $equivSet
            )
        ...
    ]
    $private:*
)
=>
((OpName)
    (Select
        $left
        [
            (FiltersItem
                (MapJoinOpFilter $item $leftCols $equivSet)
            )
        ]
    )
    (Select
        $right
        [
            (FiltersItem
                (MapJoinOpFilter $item $rightCols $equivSet)
            )
        ]
    )
    (RemoveFiltersItem $on $item)
    $private
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-degenerate minimal instance of the rule: `before()` is an inner join with ON `(L.0=R.0 ∧ L.1=R.1 ∧ L.0=R.1)`, and `after()` pushes the single mappable cross equality (`L.0=R.1`) down as `L.0=L.1` on the left and `R.0=R.1` on the right using the equivalence groups established by the two supporting equalities, then drops it from the ON — exactly the `MapJoinOpFilter`/`RemoveFiltersItem` transformation, with concrete `EQUALS` being the correct (not over-constraining) choice since the rule reasons about equality-based equivalence groups that uninterpreted `pred(...)` symbols can't express. Symbols are shared correctly (`e00`/`e11` retained in both ONs, `e01` removed; `fL`/`fR` distinct side filters), so the proof is of the real rule instance rather than a coincidental `before==after`, and the `SCOPE: PARTIAL` line honestly and specifically discloses the genuine narrowing to inner-only / two-column / one-mappable-plus-two-supporting equalities, which is a real, non-trivial family (the equivalence requires genuine transitivity reasoning, not structural identity).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7896333
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 37385875
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 846709
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 565042
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19646792
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 37494375
  },
  "total_duration": {
    "secs": 0,
    "nanos": 72593750
  }
}
```
