# Name: PruneWithCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneWithCols pushes a Project operator beneath a With. It's ok to
unconditionally push this Project down, since in the pruning case, we're
getting the project closer to the source of any prune requests, and if we
just end up with a Project incidentally, it's safe to just always push it
down.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneWithCols`, not the other rules in that file):

```
# PruneWithCols pushes a Project operator beneath a With. It's ok to
# unconditionally push this Project down, since in the pruning case, we're
# getting the project closer to the source of any prune requests, and if we
# just end up with a Project incidentally, it's safe to just always push it
# down.
[PruneWithCols, Normalize]
(Project
    (With $binding:* $input:* $private:*)
    $projections:*
    $passthrough:*
)
=>
(With
    $binding
    (Project $input $projections $passthrough)
    $private
)
```
