# FoldIsNullProject

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 28  **Verification rounds used:** 2
**Scope detail:** fixed to a single "x IS NULL" projection on a NOT NULL column x (plus a nullable passthrough column); the source rule folds an arbitrary non-empty set of such projections.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/project.opt

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
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's core — a Project item `x IS NULL` over an input column x guaranteed NOT NULL (encoded via C_Type's nullable=false, exactly mirroring `IsColNotNull`) is folded to the `false` literal while other columns pass through unchanged — and the proof is non-vacuous, since `before()` and `after()` are structurally distinct and the equivalence genuinely depends on the NOT NULL guarantee (flipping it would make the claim false), so the precondition is present, not silently dropped. The PARTIAL scope line is accurate: the DSL cannot quantify over an arbitrary non-empty set of foldable projections, so fixing one IS NULL projection plus one passthrough column is a genuine, non-degenerate minimal instance of the Optgen rule, with correct operators, no over-constrained symbol sharing, and no hardcoded symbol where an uninterpreted one was available.

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
    "nanos": 697000
  }
}
```
