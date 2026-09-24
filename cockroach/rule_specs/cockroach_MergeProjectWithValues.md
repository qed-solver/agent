# Name: MergeProjectWithValues
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/project.opt

MergeProjectWithValues merges an outer Project operator with an inner Values
operator that has a single row, as long as:

1. The Values operator has a single row (since if not, the projections would
need to replicated for each row, which is undesirable).

2. The projections do not reference Values columns, since combined Values
columns cannot reference one another.

This rule has the side effect of pruning unused columns of the Values
operator.

Extracted from `project.opt` (which defines multiple rules — implement specifically `MergeProjectWithValues`, not the other rules in that file):

```
# MergeProjectWithValues merges an outer Project operator with an inner Values
# operator that has a single row, as long as:
#
#   1. The Values operator has a single row (since if not, the projections would
#      need to replicated for each row, which is undesirable).
#
#   2. The projections do not reference Values columns, since combined Values
#      columns cannot reference one another.
#
# This rule has the side effect of pruning unused columns of the Values
# operator.
[MergeProjectWithValues, Normalize]
(Project
    $input:(Values [ * ])
    $projections:* &
        ^(AreProjectionsCorrelated
            $projections
            (OutputCols $input)
        )
    $passthrough:*
)
=>
(MergeProjectWithValues $projections $passthrough $input)
```
