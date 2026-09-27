# HoistJoinProjectRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** left join (source rule also covers inner-join and the apply variants) with a 1-column left input and 2-column right input, the passthrough project swapping the right's two columns, and the on-clause an uninterpreted predicate over (left, right) columns


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

HoistJoinProjectRight lifts a passthrough Project operator from within a Join
operator's right input to outside the join. This often allows the Project
operator to be merged with an outer Project. Since Project operators tend to
prevent other rules from matching, this and other rules try to either push
them down (to prune columns), or else to pull them up (to get them out of the
way of other operators).

It's not always beneficial to hoist projections above joins, but we need some
projection hoisting to happen to help join reordering, and doing it all in an
exploration rule risks creating a huge number of plans when combined with join
reordering.

Projections are allowed in the case when they are simple remaps from input to
output column IDs, in which case it is simple to replace the column references
in the join condition.

TODO(andyk): Add other join types.

Extracted from `join.opt` (which defines multiple rules — implement specifically `HoistJoinProjectRight`, not the other rules in that file):

```
# HoistJoinProjectRight lifts a passthrough Project operator from within a Join
# operator's right input to outside the join. This often allows the Project
# operator to be merged with an outer Project. Since Project operators tend to
# prevent other rules from matching, this and other rules try to either push
# them down (to prune columns), or else to pull them up (to get them out of the
# way of other operators).
#
# It's not always beneficial to hoist projections above joins, but we need some
# projection hoisting to happen to help join reordering, and doing it all in an
# exploration rule risks creating a huge number of plans when combined with join
# reordering.
#
# Projections are allowed in the case when they are simple remaps from input to
# output column IDs, in which case it is simple to replace the column references
# in the join condition.
#
# TODO(andyk): Add other join types.
[HoistJoinProjectRight, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply
    $left:*
    $right:(Project
        $input:*
        $projections:* &
            (AllAreRemappingProjections $projections) &

            # Ensure that there are no outer-column references in the
            # projections, since otherwise hoisting the Project could change
            # the result of a left-join due to the NULL-extended rows.
            # TODO(drewk): we could allow this for inner-joins.
            (ColsAreSubset
                (ProjectionOuterCols $projections)
                (OutputCols $input)
            )
        $passThrough:*
    )
    $on:*
    $private:*
)
=>
(Project
    ((OpName)
        $left
        $input
        (UnbindFiltersFromProjections $projections $on)
        $private
    )
    $projections
    (UnionCols (OutputCols $left) $passThrough)
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully mirrors the source's HoistJoinProjectRight: the right input's passthrough project (a pure column remap here — the swap (r1,r0)) is hoisted above the join, the same uninterpreted on-predicate C is re-based onto the un-remapped input columns in the correct swapped order (condAfter feeds C the values (l0, r1, r0), matching condBefore), and the extra outer project above the after-join is exactly what the source's `(UnionCols ...)` output-column reconstruction requires under the DSL's positional (not column-ID) model. The SCOPE line is honest about the genuine narrowing (LEFT join only vs. the source's Inner/Left + Apply variants; fixed 1-column left and 2-column right inputs; the remap restricted to the two-column swap), the before/after plans are structurally distinct and non-vacuous, and no precondition from the source (remap-only projection, no outer-column references in the projection) is silently violated.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 14173460
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 47960042
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 879000
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 958083
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 31507875
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 48177667
  },
  "total_duration": {
    "secs": 0,
    "nanos": 95328375
  }
}
```
