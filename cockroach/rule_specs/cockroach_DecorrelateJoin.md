# Name: DecorrelateJoin
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

DecorrelateJoin maps an apply join into the corresponding join without an
apply if the right side of the join is not correlated with the left side.
This allows the optimizer to consider additional physical join operators that
are unable to handle correlated inputs.

NOTE: Keep this before other decorrelation patterns, as if the correlated
join can be removed first, it avoids unnecessarily matching other
patterns that only exist to get to this pattern.

Citations: [3]

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `DecorrelateJoin`, not the other rules in that file):

```
# DecorrelateJoin maps an apply join into the corresponding join without an
# apply if the right side of the join is not correlated with the left side.
# This allows the optimizer to consider additional physical join operators that
# are unable to handle correlated inputs.
#
# NOTE: Keep this before other decorrelation patterns, as if the correlated
#       join can be removed first, it avoids unnecessarily matching other
#       patterns that only exist to get to this pattern.
#
# Citations: [3]
[DecorrelateJoin, Normalize]
(JoinApply
    $left:*
    $right:* & ^(IsCorrelated $right (OutputCols $left))
    $on:*
    $private:*
)
=>
(ConstructNonApplyJoin (OpName) $left $right $on $private)
```
