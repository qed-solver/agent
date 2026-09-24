# Name: FoldIsNullProject
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/project.opt

FoldIsNullProject folds "x IS NULL" projections to false if "x" is not null in
the Project's input. It matches if there is at least one projection that can
be folded, and it replaces all projections that can be folded.

Extracted from `project.opt` (which defines multiple rules — implement specifically `FoldIsNullProject`, not the other rules in that file):

```
# FoldIsNullProject folds "x IS NULL" projections to false if "x" is not null in
# the Project's input. It matches if there is at least one projection that can
# be folded, and it replaces all projections that can be folded.
[FoldIsNullProject, Normalize]
(Project
    $input:*
    $projections:[
            ...
            $item:(ProjectionsItem (Is (Variable $col:*) (Null)))
            ...
        ] &
        (IsColNotNull $col $input)
    $passthrough:*
)
=>
(Project
    $input
    (FoldIsNullProjectionsItems $projections $input)
    $passthrough
)
```
