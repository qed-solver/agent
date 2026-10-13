# PushLeakproofFiltersIntoPermeableBarrier

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 46  **Verification rounds used:** 3
**Scope detail:** the barrier's input is fixed to a single column; the original rule applies to any arity


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

PushLeakproofFiltersIntoPermeableBarrier splits filter expressions based on
leakproofness and pushes only the leakproof filters beneath a permeable
Barrier. The remaining filters stay above the Barrier.

This allows safe reordering of leakproof expressions while preserving the
Barrier to block unsafe transformations involving non-leakproof filters.
The Barrier must be marked as LeakproofPermeable to allow this behavior.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushLeakproofFiltersIntoPermeableBarrier`, not the other rules in that file):

```
# PushLeakproofFiltersIntoPermeableBarrier splits filter expressions based on
# leakproofness and pushes only the leakproof filters beneath a permeable
# Barrier. The remaining filters stay above the Barrier.
#
# This allows safe reordering of leakproof expressions while preserving the
# Barrier to block unsafe transformations involving non-leakproof filters.
# The Barrier must be marked as LeakproofPermeable to allow this behavior.
[PushLeakproofFiltersIntoPermeableBarrier, Normalize]
(Select
    (Barrier
        $input:*
        $leakproofPermeable:* & (If $leakproofPermeable)
    )
    $filters:* &
        (Let
            (
                $leakproofFilters
                $remainingFilters
                $ok
            ):(SplitLeakproofFilters $filters)
            $ok
        )
)
=>
(Select
    (Barrier
        (Select $input $leakproofFilters)
        $leakproofPermeable
    )
    $remainingFilters
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's logical content: a leakproof predicate is pushed from above an identity operation (the Barrier) to below it, while the remaining predicate stays above, which is exactly what the source rule rewrites. The barrier-as-identity-projection modeling is correct since a Barrier has no bag-semantic effect on rows, the uninterpreted predicates `Leakproof` and `Remaining` are properly shared across both sides, and the single-column arity restriction (the only narrowing) is honestly documented in the SCOPE line and does not change the logical principle being proved, since filter-through-identity commutativity is arity-independent. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 3625791
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 6778500
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 160667
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 237250
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 7704459
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 6822708
  },
  "total_duration": {
    "secs": 0,
    "nanos": 17282292
  }
}
```
