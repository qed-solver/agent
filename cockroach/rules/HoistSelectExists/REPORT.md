# HoistSelectExists

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 44  **Verification rounds used:** 3
**Scope detail:** the hoisted EXISTS subquery is uncorrelated (references only its own input S), so the empty SemiJoinApply condition is a SEMI join under a constant-true predicate.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistSelectExists extracts existential subqueries from Select filters,
turning them into semi-joins. This eliminates the subquery, which is often
expensive to execute and restricts the optimizer's plan choices.

This rule is marked as low priority so that it runs after other rules like
filter pushdown. Hoisting a correlated subquery is an expensive operation that
can't be undone, so do it only once all other work is complete. For example,
filter pushdown rules might be able to move the subquery nearer to the input
to which it's correlated before it's hoisted, making it easier to decorrelate.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistSelectExists`, not the other rules in that file):

```
# HoistSelectExists extracts existential subqueries from Select filters,
# turning them into semi-joins. This eliminates the subquery, which is often
# expensive to execute and restricts the optimizer's plan choices.
#
# This rule is marked as low priority so that it runs after other rules like
# filter pushdown. Hoisting a correlated subquery is an expensive operation that
# can't be undone, so do it only once all other work is complete. For example,
# filter pushdown rules might be able to move the subquery nearer to the input
# to which it's correlated before it's hoisted, making it easier to decorrelate.
[HoistSelectExists, Normalize, LowPriority]
(Select
    $input:*
    $filters:[
        ...
        $item:* &
            (HasHoistableSubquery $item) &
            (FiltersItem (Exists $subquery:*))
        ...
    ]
)
=>
(Select
    (SemiJoinApply $input $subquery [] (EmptyJoinPrivate))
    (RemoveFiltersItem $filters $item)
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the uncorrelated-special-case of HoistSelectExists: before() is Filter(And(EXISTS(Filter(S,sub_pred)), rest), L) and after() is Filter(rest, L SEMI-join Filter(S,sub_pred) ON true), which is structurally distinct (nested subquery vs. flat semi-join), uses correct uninterpreted symbols for input/subquery/remaining-filters, the SEMI+true correctly models SemiJoinApply with empty condition in the uncorrelated case, and the SCOPE:PARTIAL tag accurately names the one assumption (uncorrelated subquery) that the full rule does not require.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7072668
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35850875
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 60709
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 462875
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 14592583
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35899750
  },
  "total_duration": {
    "secs": 0,
    "nanos": 62616292
  }
}
```
