# PushLeakproofJoinIntoPermeableBarrierRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** both join inputs fixed to two columns and the join fixed to a plain inner join (the source rule applies to any arity and also matches the apply-join variant)


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

PushLeakproofJoinIntoPermeableBarrierRight is the right-side variant.
It moves a join below a permeable Barrier on its right input when all ON
filters are leakproof, then rewraps the join in the Barrier to preserve its
blocking behavior for non-leakproof expressions higher in the plan. This rule
is effectively pushing the right input into the Barrier, so the right input
must be leakproof as well.

Extracted from `join.opt` (which defines multiple rules — implement specifically `PushLeakproofJoinIntoPermeableBarrierRight`, not the other rules in that file):

```
# PushLeakproofJoinIntoPermeableBarrierRight is the right-side variant.
# It moves a join below a permeable Barrier on its right input when all ON
# filters are leakproof, then rewraps the join in the Barrier to preserve its
# blocking behavior for non-leakproof expressions higher in the plan. This rule
# is effectively pushing the right input into the Barrier, so the right input
# must be leakproof as well.
[PushLeakproofJoinIntoPermeableBarrierRight, Normalize]
(InnerJoin | InnerJoinApply
    $left:* & (IsLeakproof $left)
    (Barrier
        $right:*
        $leakproofPermeable:* & (If $leakproofPermeable)
    )
    $on:* & (HasAllLeakProofFilters $on)
    $private:*
)
=>
(Barrier
    ((OpName) $left $right $on $private)
    $leakproofPermeable
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding captures the rule's bag-semantic core exactly — Join(L, Barrier(R), C) vs Barrier(Join(L, R, C)) with the Barrier modeled as an identity projection — and before()/after() are structurally different (identity projection nested under the right join input vs. wrapping the whole join), so the proof is not vacuous; L, R, and C are correctly shared as the same uninterpreted symbols across both sides, with no spurious over-constraint. The only deviations are honestly and specifically declared in the SCOPE line (plain INNER instead of also InnerJoinApply, whose correlated/dependent-join semantics are outside the bag model, and a 2-column arity that is semantically neutral since the equivalence does not depend on arity), and the rule's IsLeakproof / HasAllLeakProofFilters / leakproofPermeable preconditions are planner-safety flags with no effect on the row bag, so no data-semantic precondition was silently dropped. ```

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
    "nanos": 422416
  }
}
```
