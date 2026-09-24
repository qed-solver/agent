# Name: MergeProjects
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/project.opt

MergeProjects merges an outer Project operator with an inner Project operator
if there are no references to the inner synthesized columns. This has the
side effect of pruning unused synthesized columns of the inner Project.

Extracted from `project.opt` (which defines multiple rules — implement specifically `MergeProjects`, not the other rules in that file):

```
# MergeProjects merges an outer Project operator with an inner Project operator
# if there are no references to the inner synthesized columns. This has the
# side effect of pruning unused synthesized columns of the inner Project.
[MergeProjects, Normalize]
(Project
    $input:(Project $innerInput:* $innerProjections:*)
    $projections:* &
        (CanMergeProjections $projections $innerProjections)
    $passthrough:*
)
=>
(Project
    $innerInput
    (MergeProjections
        $projections
        $innerProjections
        $passthrough
    )
    (DifferenceCols
        $passthrough
        (ProjectionCols $innerProjections)
    )
)
```
