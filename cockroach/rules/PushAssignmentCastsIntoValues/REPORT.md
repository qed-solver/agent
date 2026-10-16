# PushAssignmentCastsIntoValues

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 104  **Verification rounds used:** 6
**Scope detail:** input Values is modeled as an uninterpreted relation and the casted values rows as an equivalent inner projection layer (core language has no expression-row Values), with the other projection a constant.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/project.opt

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
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully mirrors the rule's structure in a non-degenerate special case: an uninterpreted single-column Values, one uninterpreted cast projection moved from the outer project into the base relation (modeled as an inner projection layer, since the DSL has no expression-row Values), and one independent constant projection carried through both sides with matching output column order — and before() and after() are structurally distinct, so the proof establishes a genuine projection-commutation/bag-preservation claim rather than a vacuous identity. The source rule's guards (cast source not referenced by other projections, not a passthrough column, at least one pushable cast) are enforced by the encoding's shape, so no precondition is silently dropped, and the PARTIAL scope line honestly names the 1-column/constant-other restriction.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 0
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 0
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 0
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 0
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 0
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 382667
  }
}
```
