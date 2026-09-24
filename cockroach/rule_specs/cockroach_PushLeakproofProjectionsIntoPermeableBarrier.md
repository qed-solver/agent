# Name: PushLeakproofProjectionsIntoPermeableBarrier
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/project.opt

PushLeakproofProjectionsIntoPermeableBarrier moves a Project below a Barrier
when all projection expressions are leakproof and the Barrier is marked as
permeable. This is safe because leakproof expressions cannot reveal
information through their evaluation, and a permeable Barrier allows such
projections to pass through it.

Extracted from `project.opt` (which defines multiple rules — implement specifically `PushLeakproofProjectionsIntoPermeableBarrier`, not the other rules in that file):

```
# PushLeakproofProjectionsIntoPermeableBarrier moves a Project below a Barrier
# when all projection expressions are leakproof and the Barrier is marked as
# permeable. This is safe because leakproof expressions cannot reveal
# information through their evaluation, and a permeable Barrier allows such
# projections to pass through it.
[PushLeakproofProjectionsIntoPermeableBarrier, Normalize]
(Project
    (Barrier
        $input:*
        $leakproofPermeable:* & (If $leakproofPermeable)
    )
    $projections:* & (HasAllLeakProofProjections $projections)
    $passthrough:*
)
=>
(Barrier
    (Project $input $projections $passthrough)
    $leakproofPermeable
)
```
