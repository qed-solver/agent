# Name: SimplifyIntersectRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/set.opt

SimplifyIntersectRight converts an Intersect operator into an IntersectAll
operator when the right input has a key. This avoids the de-duplication step.

This works because IntersectAll creates a one-to-one mapping between left and
right rows. If there is more than one row with a particular value on the
left side, then there must be at least that many rows with the same value on
the right side in order for the left rows to be preserved in the output.
Therefore, if the right input has a strict key, the output rows will be
de-duplicated for 'free', and an IntersectAll can safely be used.

Extracted from `set.opt` (which defines multiple rules — implement specifically `SimplifyIntersectRight`, not the other rules in that file):

```
# SimplifyIntersectRight converts an Intersect operator into an IntersectAll
# operator when the right input has a key. This avoids the de-duplication step.
#
# This works because IntersectAll creates a one-to-one mapping between left and
# right rows. If there is more than one row with a particular value on the
# left side, then there must be at least that many rows with the same value on
# the right side in order for the left rows to be preserved in the output.
# Therefore, if the right input has a strict key, the output rows will be
# de-duplicated for 'free', and an IntersectAll can safely be used.
[SimplifyIntersectRight, Normalize]
(Intersect $left:* $right:* & (HasStrictKey $right) $colMap:*)
=>
(IntersectAll $left $right $colMap)
```
