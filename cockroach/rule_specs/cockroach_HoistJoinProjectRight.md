# Name: HoistJoinProjectRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

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
