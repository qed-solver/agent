# Name: PushColumnRemappingIntoValues
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/project.opt

PushColumnRemappingIntoValues folds ProjectionsItems into the passthrough set
if they simply remap Values output columns that are not already in
passthrough. The Values output columns are replaced with the corresponding
columns projected by the folded ProjectionsItems.

Example:

project
├── columns: x:2!null
├── values
│    ├── columns: column1:1!null
│    ├── cardinality: [2 - 2]
│    ├── (1,)
│    └── (2,)
└── projections
└── column1:1 [as=x:2, outer=(1)]
=>
project
├── columns: x:2!null
└── values
├── columns: x:2!null
├── cardinality: [2 - 2]
├── (1,)
└── (2,)

This allows other rules to fire. In the example above, the project would now
be removed by EliminateProject.

Extracted from `project.opt` (which defines multiple rules — implement specifically `PushColumnRemappingIntoValues`, not the other rules in that file):

```
# PushColumnRemappingIntoValues folds ProjectionsItems into the passthrough set
# if they simply remap Values output columns that are not already in
# passthrough. The Values output columns are replaced with the corresponding
# columns projected by the folded ProjectionsItems.
#
# Example:
#
# project
#  ├── columns: x:2!null
#  ├── values
#  │    ├── columns: column1:1!null
#  │    ├── cardinality: [2 - 2]
#  │    ├── (1,)
#  │    └── (2,)
#  └── projections
#       └── column1:1 [as=x:2, outer=(1)]
# =>
# project
#  ├── columns: x:2!null
#  └── values
#       ├── columns: x:2!null
#       ├── cardinality: [2 - 2]
#       ├── (1,)
#       └── (2,)
#
# This allows other rules to fire. In the example above, the project would now
# be removed by EliminateProject.
[PushColumnRemappingIntoValues, Normalize]
(Project
    $input:(Values)
    $projections:*
    $passthrough:* &
        (CanPushColumnRemappingIntoValues
            $projections
            $passthrough
            $input
        )
)
=>
(PushColumnRemappingIntoValues $input $projections $passthrough)
```
