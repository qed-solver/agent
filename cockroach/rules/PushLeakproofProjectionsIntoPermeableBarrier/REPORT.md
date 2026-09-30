# PushLeakproofProjectionsIntoPermeableBarrier

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 25  **Verification rounds used:** 2
**Scope detail:** the barrier's input is fixed to a single column with exactly one (uninterpreted) projection expression and one passthrough column; the original rule applies to any arity


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/project.opt

PushLeakproofProjectionsIntoPermeableBarrier moves a Project below a Barrier
when all projection expressions are leakproof and the Barrier is marked as
permeable. This is safe because leakproof expressions cannot reveal
information through their evaluation, and a permeable Barrier allows such
projections to pass through it.

Extracted from `project.opt` (which defines multiple rules — implement specifically `PushLeakproofProjectionsIntoPermeableBarrier`, not the other rules in that file):

```
# PushLeakproofProjectionsIntoPermeableBarrier moves a Project below a Barrier
# when all projection expressions are leakproof and the Barrier is marked as
# permeable. This is safe because leakproof expressions cannot reveal
# information through their evaluation, and a permeable Barrier allows such
# projections to pass through it.
[PushLeakproofProjectionsIntoPermeableBarrier, Normalize]
(Project
    (Barrier
        $input:*
        $leakproofPermeable:* & (If $leakproofPermeable)
    )
    $projections:* & (HasAllLeakProofProjections $projections)
    $passthrough:*
)
=>
(Barrier
    (Project $input $projections $passthrough)
    $leakproofPermeable
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The before/after shapes genuinely reflect the optimization — the Project moves from above the barrier to below it (not a structural identity) — and the barrier is modeled as a row-identity projection, the only row-level behavior a barrier has, with the single uninterpreted projection operator and passthrough field correctly shared as the same symbols on both sides. The rule's HasAllLeakProofProjections/(If $leakproofPermeable) preconditions are security properties of expression evaluation, not constraints on row values, so they cannot be expressed in QED's bag model (a real QED limitation, acknowledged in the code comments); the proved unconditional row-level equivalence is therefore the strongest row-semantic claim the rule needs and subsumes it. The fixed 1-column / 1-expression / 1-passthrough arity is the minimal shape in which the rule can exist, is specific and honestly disclosed in the PARTIAL tag (full variadic arity is not expressible in the DSL at any width), and is not degenerate. ```

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
    "nanos": 416959
  }
}
```
