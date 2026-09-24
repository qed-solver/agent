# Name: PushLeakproofJoinIntoPermeableBarrierRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

PushLeakproofJoinIntoPermeableBarrierRight is the right-side variant.
It moves a join below a permeable Barrier on its right input when all ON
filters are leakproof, then rewraps the join in the Barrier to preserve its
blocking behavior for non-leakproof expressions higher in the plan. This rule
is effectively pushing the right input into the Barrier, so the right input
must be leakproof as well.

Extracted from `join.opt` (which defines multiple rules — implement specifically `PushLeakproofJoinIntoPermeableBarrierRight`, not the other rules in that file):

```
# PushLeakproofJoinIntoPermeableBarrierRight is the right-side variant.
# It moves a join below a permeable Barrier on its right input when all ON
# filters are leakproof, then rewraps the join in the Barrier to preserve its
# blocking behavior for non-leakproof expressions higher in the plan. This rule
# is effectively pushing the right input into the Barrier, so the right input
# must be leakproof as well.
[PushLeakproofJoinIntoPermeableBarrierRight, Normalize]
(InnerJoin | InnerJoinApply
    $left:* & (IsLeakproof $left)
    (Barrier
        $right:*
        $leakproofPermeable:* & (If $leakproofPermeable)
    )
    $on:* & (HasAllLeakProofFilters $on)
    $private:*
)
=>
(Barrier
    ((OpName) $left $right $on $private)
    $leakproofPermeable
)
```
