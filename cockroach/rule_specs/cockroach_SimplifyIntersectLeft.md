# Name: SimplifyIntersectLeft
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/set.opt

SimplifyIntersectLeft converts an Intersect operator into an IntersectAll
operator when the left input has a key. This avoids the de-duplication step.

Extracted from `set.opt` (which defines multiple rules — implement specifically `SimplifyIntersectLeft`, not the other rules in that file):

```
# SimplifyIntersectLeft converts an Intersect operator into an IntersectAll
# operator when the left input has a key. This avoids the de-duplication step.
[SimplifyIntersectLeft, Normalize]
(Intersect $left:* & (HasStrictKey $left) $right:* $colMap:*)
=>
(IntersectAll $left $right $colMap)
```
