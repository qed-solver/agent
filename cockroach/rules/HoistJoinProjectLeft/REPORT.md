# HoistJoinProjectLeft

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** inner join only; the left input's project is a pure column reordering (swap) of the raw left input and the join condition is a single uninterpreted predicate


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

HoistJoinProjectLeft is the same as HoistJoinProjectRight, but for the left
input of the join.

Extracted from `join.opt` (which defines multiple rules — implement specifically `HoistJoinProjectLeft`, not the other rules in that file):

```
# HoistJoinProjectLeft is the same as HoistJoinProjectRight, but for the left
# input of the join.
[HoistJoinProjectLeft, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply
    $left:(Project
        $input:*
        $projections:* & ^(HasVolatileProjection $projections)
        $passThrough:*
    )
    $right:* &

        # For apply-joins, the right input could reference the projected
        # columns, in which case pulling the Project up would be incorrect.
        # This isn't an issue for HoistJoinProjectRight because outer column
        # references cannot be from the left input to the right input.
        # TODO(drewk): we could remap the right input as well.
        ^(IsCorrelated $right (ProjectionCols $projections))
    $on:*
    $private:* &

        # We can only hoist the projection if each new column is either:
        # 1. a simple remapping that can be reversed in the join condition, OR
        # 2. not referenced in the join condition AND
        # 3. the projection does not reference input columns
        #
        # The last condition, (3), is a very conservative heuristic to avoid
        # hoisting projections that could prevent column pruning. We might be
        # able to remove it or make it smarter.
        #
        # TODO(michae2): we could work around (2) by inlining the projection
        # expression into the join condition, similar to
        # PushSelectIntoInlinableProject.
        (Let
            ($remap $other $ok):(CanHoistNonRemappingProjections
                $projections
            )
            $ok
        ) &
        ^(ColsIntersect
            (FilterOuterCols $on)
            (ProjectionCols $other)
        ) &
        ^(ColsIntersect
            (ProjectionOuterCols $other)
            (OutputCols $input)
        )
)
=>
(Project
    ((OpName)
        $input
        $right
        (UnbindFiltersFromProjections $remap $on)
        $private
    )
    $projections
    (UnionCols $passThrough (OutputCols $right))
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The proved identity is non-vacuous and is exactly the rule's core transformation: `before()` = Join_C(π(L), R) and `after()` = π(Join_{C∘π}(L, R)) are structurally different plans (project below vs. above the join) over uninterpreted tables, uninterpreted types, and one shared uninterpreted condition, and the unbinding bookkeeping is consistent — the condition evaluates C on (L1, L0, R) in both encodings (join-row ordinals (0,1,2) before, (1,0,2) after) and the outer project restores the (L1, L0, R) output, so the bags match for every instantiation. Every side condition of the source rule (¬HasVolatileProjection, ¬IsCorrelated on the right input, the ColsIntersect checks on non-remap projections) is vacuously satisfied by a pure column swap on an INNER join, so no precondition is silently missing, and the `SCOPE: PARTIAL` line honestly and specifically names the genuine restrictions (INNER only vs. the rule's InnerJoin/InnerJoinApply/LeftJoin/LeftJoinApply set; a fixed two-column remap swap vs. arbitrary hoistable projections; a single uninterpreted predicate) — a legitimate minimal instance, not a degenerate or misleading one.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7317624
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 33107833
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 894917
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 492250
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19718083
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 33222917
  },
  "total_duration": {
    "secs": 0,
    "nanos": 68453042
  }
}
```
