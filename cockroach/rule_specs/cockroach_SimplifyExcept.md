# Name: SimplifyExcept
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/set.opt

SimplifyExcept converts an Except operator into an ExceptAll operator when the
left input has a key. This avoids the de-duplication step.

Extracted from `set.opt` (which defines multiple rules — implement specifically `SimplifyExcept`, not the other rules in that file):

```
# SimplifyExcept converts an Except operator into an ExceptAll operator when the
# left input has a key. This avoids the de-duplication step.
[SimplifyExcept, Normalize]
(Except $left:* & (HasStrictKey $left) $right:* $colMap:*)
=>
(ExceptAll $left $right $colMap)
```
