# PushColumnRemappingIntoValues

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 25  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/project.opt

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
```

## Independent verifier review

**Verdict:** AGREE

The rule's only effect is renaming a Values column and moving a plain column-reference projection item into the passthrough set — a pure column-identity/metadata transformation with zero row-bag delta, and QED's nameless positional model (where both a passthrough column and a column-ref projection item serialize to the identical `{"column": i}` reference, and Values/Project column names carry no semantics) makes before and after the same object in the model, so any encoding would prove a vacuous identity rather than the rule. This gap lives in the fixed Rust prover's representation, not in a closable DSL builder: even extending the DSL with a Values-with-rows operator would only yield proofs of "identity projection is identity," which hold independently of the rule's conditions.
