# PushColumnRemappingIntoValues

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 37  **Verification rounds used:** 3

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

PushColumnRemappingIntoValues is a data-preserving hygiene step — it renames a Values column to a projection's alias and folds that identity (Variable-reference) projection from the computed set into passthrough, leaving the rows unchanged — so its only semantic delta is a column rename, which QED's bag-of-tuples model has no first-class notion of (it tracks neither column identity/name nor a non-empty Values node, only the empty one). Hence every faithful before/after encoding collapses to the vacuous identity-projection law (the same bag of tuples ≡ itself), whereas any encoding that introduces separate value relations to manufacture checkable content models them as unrelated uninterpreted relations and is unprovable; no encoding at any level of generality yields a rule-specific bag-semantic statement, so the UNSUPPORTED claim is sound. ```
