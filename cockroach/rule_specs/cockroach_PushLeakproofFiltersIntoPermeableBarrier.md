# Name: PushLeakproofFiltersIntoPermeableBarrier
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/select.opt

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
