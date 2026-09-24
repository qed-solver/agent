# Name: PushAssignmentCastsIntoValues
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/project.opt

PushAssignmentCastsIntoValues pushes assignment cast projections into Values
rows.

Example:

project
├── columns: x:2 y:3
├── values
│    ├── columns: column1:1
│    ├── cardinality: [2 - 2]
│    ├── (1,)
│    └── (2,)
└── projections
├── assignment-cast: STRING [as=x:2]
│    └── column1:1
└── 'foo' [as=y:3]
=>
project
├── columns: x:2 y:3
├── values
│    ├── columns: x:2
│    ├── cardinality: [2 - 2]
│    ├── tuple
│    │    └── assignment-cast: STRING
│    │        └── 1
│    └── tuple
│         └── assignment-cast: STRING
│             └── 2
└── projections
└── 'foo' [as=y:3]

This allows other rules to fire, with the ultimate goal of eliminating the
project so that the insert fast-path optimization is used in more cases and
uniqueness checks for gen_random_uuid() values are eliminated in more cases.

Assignment casts in projections cannot be pushed into values expressions if
the casted column is referenced in another projection expression
(AssignmentCastCols ensures this) or if the casted column also a passthrough
column (notice the DifferenceCols function).

Extracted from `project.opt` (which defines multiple rules — implement specifically `PushAssignmentCastsIntoValues`, not the other rules in that file):

```
# PushAssignmentCastsIntoValues pushes assignment cast projections into Values
# rows.
#
# Example:
#
# project
#  ├── columns: x:2 y:3
#  ├── values
#  │    ├── columns: column1:1
#  │    ├── cardinality: [2 - 2]
#  │    ├── (1,)
#  │    └── (2,)
#  └── projections
#       ├── assignment-cast: STRING [as=x:2]
#       │    └── column1:1
#       └── 'foo' [as=y:3]
# =>
# project
#  ├── columns: x:2 y:3
#  ├── values
#  │    ├── columns: x:2
#  │    ├── cardinality: [2 - 2]
#  │    ├── tuple
#  │    │    └── assignment-cast: STRING
#  │    │        └── 1
#  │    └── tuple
#  │         └── assignment-cast: STRING
#  │             └── 2
#  └── projections
#       └── 'foo' [as=y:3]
#
# This allows other rules to fire, with the ultimate goal of eliminating the
# project so that the insert fast-path optimization is used in more cases and
# uniqueness checks for gen_random_uuid() values are eliminated in more cases.
#
# Assignment casts in projections cannot be pushed into values expressions if
# the casted column is referenced in another projection expression
# (AssignmentCastCols ensures this) or if the casted column also a passthrough
# column (notice the DifferenceCols function).
[PushAssignmentCastsIntoValues, Normalize]
(Project
    $input:(Values)
    $projections:*
    $passthrough:* &
        ^(ColsAreEmpty
            $castCols:(IntersectionCols
                (DifferenceCols
                    (AssignmentCastCols $projections)
                    $passthrough
                )
                (OutputCols $input)
            )
        )
)
=>
(PushAssignmentCastsIntoValues
    $input
    $projections
    $passthrough
    $castCols
)
```
