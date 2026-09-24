# Name: PushLeakproofJoinIntoPermeableBarrierLeft
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

PushLeakproofJoinIntoPermeableBarrierLeft moves a join below a permeable
Barrier on its left input when all ON filters are leakproof. The Barrier is
then placed above the join. This is safe because leakproof filters can be
reordered freely, and the Barrier allows such movement when marked as
LeakproofPermeable. This rule is effectively pushing the left input into the
Barrier, so the left input must be leakproof as well.

Extracted from `join.opt` (which defines multiple rules — implement specifically `PushLeakproofJoinIntoPermeableBarrierLeft`, not the other rules in that file):

```
# PushLeakproofJoinIntoPermeableBarrierLeft moves a join below a permeable
# Barrier on its left input when all ON filters are leakproof. The Barrier is
# then placed above the join. This is safe because leakproof filters can be
# reordered freely, and the Barrier allows such movement when marked as
# LeakproofPermeable. This rule is effectively pushing the left input into the
# Barrier, so the left input must be leakproof as well.
[PushLeakproofJoinIntoPermeableBarrierLeft, Normalize]
(InnerJoin | InnerJoinApply
    (Barrier
        $left:*
        $leakproofPermeable:* & (If $leakproofPermeable)
    )
    $right:* & (IsLeakproof $right)
    $on:* & (HasAllLeakProofFilters $on)
    $private:*
)
=>
(Barrier
    ((OpName) $left $right $on $private)
    $leakproofPermeable
)
```
