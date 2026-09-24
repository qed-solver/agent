# Name: HoistJoinProjectLeft
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

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
