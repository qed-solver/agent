# Name: EliminateProject
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/project.opt

EliminateProject discards a Project operator which is not adding or removing
columns.

Extracted from `project.opt` (which defines multiple rules — implement specifically `EliminateProject`, not the other rules in that file):

```
# EliminateProject discards a Project operator which is not adding or removing
# columns.
[EliminateProject, Normalize]
(Project
    $input:*
    $projections:[]
    $passthrough:* &
        (ColsAreEqual $passthrough (OutputCols $input))
)
=>
$input
```
