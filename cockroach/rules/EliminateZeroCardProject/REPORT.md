# EliminateZeroCardProject

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2
**Scope detail:** fixed-shape pattern: 2-column zero-row input with 2 passthrough columns and 1 uninterpreted projection, versus the original rule's arbitrary passthrough/projection lists.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/project.opt

EliminateZeroCardProject eliminates a Project when its input has zero
cardinality. The projection expressions are per-row and will never be
evaluated, so it is safe to remove them regardless of volatility. This
complements the SimplifyZeroCardinalityGroup rule which requires the entire
expression to be leakproof.

Extracted from `project.opt` (which defines multiple rules — implement specifically `EliminateZeroCardProject`, not the other rules in that file):

```
# EliminateZeroCardProject eliminates a Project when its input has zero
# cardinality. The projection expressions are per-row and will never be
# evaluated, so it is safe to remove them regardless of volatility. This
# complements the SimplifyZeroCardinalityGroup rule which requires the entire
# expression to be leakproof.
[EliminateZeroCardProject, Normalize]
(Project
    $input:* & (HasZeroRows $input) & (IsLeakproof $input)
    $projections:*
    $passthrough:*
)
=>
(ConstructEmptyValues
    (UnionCols $passthrough (ProjectionCols $projections))
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding correctly models the original rule's core semantic claim — a Project over a zero-cardinality input yields an empty relation of the output type — by using `.empty()` to encode `HasZeroRows` and an uninterpreted function `f` for the projection expression; the before/after trees are structurally different (Project-over-∅ vs. bare ∅) so the proof is non-vacuous, and the PARTIAL scope tag honestly documents the fixed 2-column/1-projection shape that the DSL cannot express with arbitrary arities.

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
    "nanos": 874125
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 190167
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
    "nanos": 1447959
  }
}
```
